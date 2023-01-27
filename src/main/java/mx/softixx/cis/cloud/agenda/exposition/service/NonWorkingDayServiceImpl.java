package mx.softixx.cis.cloud.agenda.exposition.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.MedicalScheduleMapper;
import mx.softixx.cis.cloud.agenda.exposition.mapper.NonWorkingDayMapper;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.NonWorkingDay;
import mx.softixx.cis.cloud.agenda.persistence.repository.NonWorkingDayRepository;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayConflictException;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayDuplicateException;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayNotFoundException;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayOutOfRangeException;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayRequest;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse.NwdResponse;
import mx.softixx.cis.common.core.collection.ListUtils;
import mx.softixx.cis.common.core.datetime.LocalDateUtils;

@Service
@Transactional(readOnly = true)
public class NonWorkingDayServiceImpl implements NonWorkingDayService {
	
	private final MedicalScheduleService medicalScheduleService;
	private final NonWorkingDayRepository nonWorkingDayRepository;
	
	public NonWorkingDayServiceImpl(MedicalScheduleService medicalScheduleService, 
								   NonWorkingDayRepository nonWorkingDayRepository) {
		this.medicalScheduleService = medicalScheduleService;
		this.nonWorkingDayRepository = nonWorkingDayRepository;
	}

	@Override
	public NonWorkingDayResponse findByMedicalSchedule(MedicalSchedule schedule) {
		return NonWorkingDayMapper.map(schedule);
	}

	@Override
	public NwdResponse findOne(MedicalSchedule schedule, LocalDate nwd) {
		val nonWorkingDay = nonWorkingDayRepository.findByMedicalScheduleAndNwd(schedule, nwd).orElse(null);
		return NonWorkingDayMapper.map(nonWorkingDay);
	}

	@Override
	@Transactional
	public NonWorkingDayResponse addOrUpdate(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		validateRequest(medicalSchedule, nonWorkingDayRequest);
		val medicalScheduleUpdated = save(medicalSchedule, nonWorkingDayRequest);
		return NonWorkingDayMapper.map(medicalScheduleUpdated);
	}

	@Override
	@Transactional
	public void delete(MedicalSchedule schedule) {
		schedule.getNonWorkingDays().forEach(this::delete);
	}
	
	@Override
	@Transactional
	public void delete(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		validateDeleteRequest(medicalSchedule, nonWorkingDayRequest);
		
		val daysToDelete = new ArrayList<NonWorkingDay>();
		nonWorkingDayRequest.getDaysToDelete().forEach(nwd -> {
			val nonWorkingDay = findByMedicalSchedule(medicalSchedule, nwd); 
			daysToDelete.add(nonWorkingDay);
		});
		nonWorkingDayRepository.deleteAll(daysToDelete);
	}

	@Override
	@Transactional
	public void delete(MedicalSchedule schedule, LocalDate nwd) {
		val nonWorkingDay = findByMedicalSchedule(schedule, nwd);
		delete(nonWorkingDay);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		val nonWorkingDay = nonWorkingDayRepository.findById(id).orElseThrow(() -> new NonWorkingDayNotFoundException(id));
		delete(nonWorkingDay);
	}
	
	private void validateRequest(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		val nonWorkingDays = nonWorkingDayRequest.getNonWorkingDays();
		
		// There must be no duplicate dates
		val hasDuplicate = ListUtils.hasDuplicateValues(nonWorkingDays);
		if (hasDuplicate) {
			throw new NonWorkingDayDuplicateException();
		}
		
		// All dates must be less than the end date of the medical schedule
		val isOutOfRange = nonWorkingDays.stream().filter(nonWorkingDayFilter(medicalSchedule)).findFirst().isPresent();
		if (isOutOfRange) {
			throw new NonWorkingDayOutOfRangeException();
		}
	}
	
	private void validateDeleteRequest(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		val nonWorkingDays = medicalSchedule.getNonWorkingDays();
		val daysToDelete = nonWorkingDayRequest.getDaysToDelete();
		
		if (nonWorkingDays.isEmpty()) {
			throw new NonWorkingDayConflictException();
		}
		
		// There must be no duplicate dates
		val hasDuplicate = ListUtils.hasDuplicateValues(daysToDelete);
		if (hasDuplicate) {
			throw new NonWorkingDayDuplicateException();
		}
		
		// All dates must be less than the end date of the medical schedule
		val isOutOfRange = daysToDelete.stream().filter(nonWorkingDayFilter(medicalSchedule)).findFirst().isPresent();
		if (isOutOfRange) {
			throw new NonWorkingDayOutOfRangeException();
		}
	}

	private static Predicate<? super LocalDate> nonWorkingDayFilter(MedicalSchedule medicalSchedule) {
		return p -> LocalDateUtils.isAfter(p, medicalSchedule.getEndDate());
	}
	
	private MedicalSchedule save(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		MedicalScheduleMapper.map(medicalSchedule, nonWorkingDayRequest);
		return medicalScheduleService.update(medicalSchedule);
	}
	
	private NonWorkingDay findByMedicalSchedule(MedicalSchedule schedule, LocalDate nwd) {
		return nonWorkingDayRepository.findByMedicalScheduleAndNwd(schedule, nwd)
				.orElseThrow(() -> new NonWorkingDayNotFoundException(nwd));
	}
	
	private void delete(NonWorkingDay nonWorkingDay) {
		if (nonWorkingDay != null) {
			nonWorkingDayRepository.delete(nonWorkingDay);
		}
	}

}