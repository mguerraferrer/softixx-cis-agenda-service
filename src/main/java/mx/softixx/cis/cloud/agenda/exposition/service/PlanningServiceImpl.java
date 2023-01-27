package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.PlanningDayMapper;
import mx.softixx.cis.cloud.agenda.exposition.mapper.PlanningMapper;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.repository.PlanningRepository;
import mx.softixx.cis.cloud.agenda.validator.AppointmentDurationValidator;
import mx.softixx.cis.common.agenda.exception.PlanningFixedConflictException;
import mx.softixx.cis.common.agenda.exception.PlanningNotFoundException;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.PlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.PlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.PlanningRequest;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;

@Service
@Transactional(readOnly = true)
public class PlanningServiceImpl implements PlanningService {

	private final PlanningFixedService planningFixedService;
	private final PrivatePracticePlanningService privatePracticePlanningService;
	private final ClinicalEntityPlanningService clinicalEntityPlanningService;
	private final PlanningRepository planningRepository;
	
	public PlanningServiceImpl(PlanningFixedService planningFixedService, 
							   PrivatePracticePlanningService privatePracticePlanningService,
							   ClinicalEntityPlanningService clinicalEntityPlanningService, 
							   PlanningRepository planningRepository) {
		this.planningFixedService = planningFixedService;
		this.privatePracticePlanningService = privatePracticePlanningService;
		this.clinicalEntityPlanningService = clinicalEntityPlanningService;
		this.planningRepository = planningRepository;
	}

	@Override
	public List<Planning> findAll(MedicalSchedule schedule) {
		return planningRepository.findByMedicalSchedule(schedule);
	}

	@Override
	public List<PlanningResponse> findByMedicalSchedule(MedicalSchedule schedule) {
		if (schedule != null) {
			val plannings = new ArrayList<PlanningResponse>();
			schedule.getPlannings().forEach(planning -> {
				val planningResponse = planningDetails(planning);
				plannings.add(planningResponse);
			});
			return plannings;
		}
		return Collections.emptyList();
	}

	@Override
	public Planning findById(Long id) {
		return planningRepository.findById(id).orElseThrow(() -> new PlanningNotFoundException(id));
	}

	@Override
	public Planning findOne(Long id) {
		return planningRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public PlanningResponse create(MedicalSchedule medicalSchedule,
			MedicalSchedulePlanningFixedRequest planningFixedRequest) {
		return privatePracticePlanningService.create(medicalSchedule, planningFixedRequest);
	}

	@Override
	@Transactional
	public PlanningResponse create(MedicalSchedule medicalSchedule,
			MedicalSchedulePlanningDayRequest planningWeekDayRequest) {
		return privatePracticePlanningService.create(medicalSchedule, planningWeekDayRequest);
	}

	@Override
	@Transactional
	public PlanningResponse create(MedicalSchedule medicalSchedule, PlanningFixedRequest planningFixedRequest) {
		val planningRequest = planningFixedRequest.getPlanning();
		val fixedRequest = planningFixedRequest.getPlanningFixed();
		return privatePracticePlanningService.create(medicalSchedule, planningRequest, fixedRequest);
	}

	@Override
	@Transactional
	public PlanningResponse create(MedicalSchedule medicalSchedule, PlanningDayRequest planningDayRequest) {
		val planningRequest = planningDayRequest.getPlanning();
		val planningDays = planningDayRequest.getPlanningDays();
		return privatePracticePlanningService.create(medicalSchedule, planningRequest, planningDays);
	}

	@Override
	@Transactional
	public PlanningResponse update(Long planningId, PlanningRequest planningRequest) {
		val planning = findById(planningId);
		
		verifyUpdateRequest(planning, planningRequest);
		
		PlanningMapper.map(planning, planningRequest);
		return saveAndReturn(planning);
	}
	
	@Override
	@Transactional
	public PlanningResponse update(Long planningId, PlanningFixedRequest planningFixedRequest) {
		val planningRequest = planningFixedRequest.getPlanning();
		val fixedRequest = planningFixedRequest.getPlanningFixed();
		
		verifyUpdateRequest(planningRequest, true);
		
		val planning = findById(planningId);
		PlanningMapper.map(planning, planningRequest);
		save(planning);
		
		val planningFixed = planningFixedService.update(planning, fixedRequest);
		return PlanningMapper.map(planning, planningFixed);
	}

	@Override
	@Transactional
	public PlanningResponse update(Long planningId, PlanningDayRequest planningDayRequest) {
		val planningRequest = planningDayRequest.getPlanning();
		val planningDays = planningDayRequest.getPlanningDays();
		
		verifyUpdateRequest(planningRequest, false);
		
		val planning = findById(planningId);
		PlanningMapper.map(planning, planningRequest);
		
		val days = planningDays.stream().map(i -> PlanningDayMapper.map(planning, i)).toList();
		planning.addPlanningDays(days);
		
		return saveAndReturn(planning);
	}
	
	@Override
	@Transactional
	public void delete(MedicalSchedule schedule) {
		schedule.getPlannings().forEach(this::delete);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		val planning = findById(id);
		delete(planning);
	}

	private PlanningResponse planningDetails(Planning planning) {
		if (planning != null) {
			PlanningResponse planningResponse;
			if (planning.getDoctorSpecialityId() != null) {
				planningResponse = privatePracticePlanningService.planningDetails(planning);
			} else {
				planningResponse = clinicalEntityPlanningService.planningDetails(planning);
			}
			return planningResponse;
		}
		return null;
	}
	
	private PlanningResponse saveAndReturn(Planning planning) {
		save(planning);
		return planningDetails(planning);
	}

	private Planning save(Planning planning) {
		if (planning != null) {
			return planningRepository.save(planning);
		}
		return null;
	}
	
	private void delete(Planning planning) {
		if (planning != null) {
			if (planning.isFixedSchedule()) {
				planningFixedService.delete(planning);
			}
			planningRepository.delete(planning);
		}
	}
	
	private boolean isPlanningFixed(Planning planning) {
		val planningFixed = planningFixedService.findOneByPlanning(planning);
		return planningFixed != null;
	}
	
	private boolean isPlanningDay(Planning planning) {
		return !planning.getPlanningDays().isEmpty();
	}
	
	private void verifyUpdateRequest(Planning planning, PlanningRequest planningRequest) {
		if (isPlanningFixed(planning)) {
			verifyUpdateRequest(planningRequest, true);
		} else if (isPlanningDay(planning)) {
			verifyUpdateRequest(planningRequest, false);
		} else {
			throw new PlanningFixedConflictException();
		}
	}

	private void verifyUpdateRequest(PlanningRequest planningRequest, boolean isFixedSchedule) {
		if (isFixedSchedule) {
			if (!planningRequest.isFixedSchedule()) {
				throw new PlanningFixedConflictException();
			}
		} else {
			if (planningRequest.isFixedSchedule()) {
				throw new PlanningFixedConflictException();
			}
		}
		AppointmentDurationValidator.validate(planningRequest.getAppointmentDuration());
	}
	
}