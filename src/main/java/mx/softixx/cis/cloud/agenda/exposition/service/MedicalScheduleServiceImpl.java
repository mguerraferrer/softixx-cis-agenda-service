package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.MedicalScheduleMapper;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.repository.MedicalScheduleRepository;
import mx.softixx.cis.cloud.agenda.validator.MedicalScheduleValidator;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleNotFoundException;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleResponse;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;

@Service
@Transactional(readOnly = true)
public class MedicalScheduleServiceImpl implements MedicalScheduleService {

	private final PlanningService planningService;
	private final MedicalScheduleRepository medicalScheduleRepository;

	public MedicalScheduleServiceImpl(PlanningService planningService,
			MedicalScheduleRepository medicalScheduleRepository) {
		this.planningService = planningService;
		this.medicalScheduleRepository = medicalScheduleRepository;
	}

	@Override
	public MedicalSchedule findById(Long id) {
		return medicalScheduleRepository.findById(id).orElseThrow(() -> new MedicalScheduleNotFoundException(id));
	}
	
	@Override
	public MedicalSchedule findByDoctorAndPrivatePractice(Long doctorId, Long privatePracticeId) {
		return medicalScheduleRepository.findByDoctorIdAndPrivatePracticeId(doctorId, privatePracticeId)
				.orElseThrow(() -> new MedicalScheduleNotFoundException(doctorId, privatePracticeId, null));
	}

	@Override
	public MedicalScheduleResponse findOneByDoctorAndPrivatePractice(Long doctorId, Long privatePracticeId) {
		val medicalSchedule = medicalScheduleRepository
				.findByDoctorIdAndPrivatePracticeId(doctorId, privatePracticeId).orElse(null);
		val plannings = planningService.findByMedicalSchedule(medicalSchedule);
		return MedicalScheduleMapper.map(medicalSchedule, plannings);
	}

	@Override
	public MedicalSchedule findByDoctorAndClinicalEntity(Long doctorId, Long clinicalEntityId) {
		return medicalScheduleRepository.findByDoctorIdAndClinicalEntityId(doctorId, clinicalEntityId)
				.orElseThrow(() -> new MedicalScheduleNotFoundException(doctorId, null, clinicalEntityId));
	}

	@Override
	public MedicalScheduleResponse findOneByDoctorAndClinicalEntity(Long doctorId, Long clinicalEntityId) {
		val medicalSchedule = medicalScheduleRepository.findByDoctorIdAndClinicalEntityId(doctorId, clinicalEntityId)
				.orElse(null);
		return MedicalScheduleMapper.map(medicalSchedule);
	}

	@Override
	@Transactional
	public MedicalScheduleResponse create(MedicalScheduleRequest request) {
		verifyRequest(request);
		val medicalSchedule = MedicalScheduleMapper.map(request);
		return saveAndReturn(medicalSchedule);
	}

	@Override
	@Transactional
	public MedicalScheduleResponse create(MedicalSchedulePlanningRequest request) {
		MedicalScheduleValidator.verifyRequest(request);
		verifyRequest(request);
		
		val medicalSchedule = MedicalScheduleMapper.map(request.getMedicalSchedule());
		save(medicalSchedule);
		if (medicalSchedule != null) {
			PlanningResponse planningResponse = null;
			if (request instanceof MedicalSchedulePlanningFixedRequest planningFixedRequest) {
				planningResponse = planningService.create(medicalSchedule, planningFixedRequest);
			} else if (request instanceof MedicalSchedulePlanningDayRequest planningWeekDayRequest) {
				planningResponse = planningService.create(medicalSchedule, planningWeekDayRequest);
			}
			val medicalScheduleResponse = MedicalScheduleMapper.map(medicalSchedule);
			medicalScheduleResponse.setPlannings(List.of(planningResponse));
			return medicalScheduleResponse;
		}
		return null;
	}

	@Override
	@Transactional
	public MedicalSchedule update(MedicalSchedule medicalSchedule) {
		return save(medicalSchedule);
	}
	
	@Override
	@Transactional
	public MedicalScheduleResponse updateByPrivatePractice(Long doctorId, 
														   Long privatePracticeId,
														   MedicalScheduleRequest request) {
		val medicalSchedule = findByDoctorAndPrivatePractice(doctorId, privatePracticeId);
		MedicalScheduleMapper.map(medicalSchedule, request);
		return updateAndReturn(medicalSchedule);
	}

	@Override
	@Transactional
	public MedicalScheduleResponse updateByClinicalEntity(Long doctorId, Long clinicalEntityId,
			MedicalScheduleRequest request) {
		val medicalSchedule = findByDoctorAndClinicalEntity(doctorId, clinicalEntityId);
		MedicalScheduleMapper.map(medicalSchedule, request);
		return updateAndReturn(medicalSchedule);
	}

	@Override
	@Transactional
	public void deleteByPrivatePractice(Long doctorId, Long privatePracticeId) {
		val medicalSchedule = findByDoctorAndPrivatePractice(doctorId, privatePracticeId);
		delete(medicalSchedule);
	}

	@Override
	@Transactional
	public void deleteByClinicalEntity(Long doctorId, Long clinicalEntityId) {
		val medicalSchedule = findByDoctorAndClinicalEntity(doctorId, clinicalEntityId);
		delete(medicalSchedule);
	}

	private void verifyRequest(MedicalScheduleRequest request) {
		val doctorId = request.getDoctorId();
		val privatePracticeId = request.getPrivatePracticeId();
		val clinicalEntityId = request.getClinicalEntityId();

		verifyRequest(doctorId, privatePracticeId, clinicalEntityId);
	}

	private void verifyRequest(MedicalSchedulePlanningRequest request) {
		val doctorId = request.getMedicalSchedule().getDoctorId();
		val privatePracticeId = request.getMedicalSchedule().getPrivatePracticeId();
		val clinicalEntityId = request.getMedicalSchedule().getClinicalEntityId();
		
		verifyRequest(doctorId, privatePracticeId, clinicalEntityId);
	}

	private void verifyRequest(final Long doctorId, final Long privatePracticeId, final Long clinicalEntityId) {
		val existsByPrivatePractice = medicalScheduleRepository
				.findByDoctorIdAndPrivatePracticeId(doctorId, privatePracticeId).isPresent();
		val existsByClinicalEntity = medicalScheduleRepository
				.findByDoctorIdAndPrivatePracticeId(doctorId, clinicalEntityId).isPresent();
		if (existsByPrivatePractice || existsByClinicalEntity) {
			throw new MedicalScheduleAlreadyExistsException(doctorId, privatePracticeId, clinicalEntityId);
		}
	}

	private MedicalScheduleResponse updateAndReturn(final MedicalSchedule medicalSchedule) {
		val medicalScheduleResponse = saveAndReturn(medicalSchedule);
		val planningResponse = planningService.findByMedicalSchedule(medicalSchedule);
		medicalScheduleResponse.setPlannings(planningResponse);
		return medicalScheduleResponse;
	}
	
	private MedicalScheduleResponse saveAndReturn(MedicalSchedule medicalSchedule) {
		save(medicalSchedule);
		return MedicalScheduleMapper.map(medicalSchedule);
	}

	private MedicalSchedule save(MedicalSchedule medicalSchedule) {
		if (medicalSchedule != null) {
			return medicalScheduleRepository.save(medicalSchedule);
		}
		return null;
	}

	private void delete(MedicalSchedule medicalSchedule) {
		if (medicalSchedule != null) {
			planningService.delete(medicalSchedule);
			medicalScheduleRepository.delete(medicalSchedule);
		}
	}

}