package mx.softixx.cis.cloud.agenda.exposition.observavility;

import org.springframework.stereotype.Component;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.service.MedicalScheduleService;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleResponse;

@Component
public class MedicalScheduleObservation {

	private final MedicalScheduleService medicalScheduleService;
	private final ObservationRegistry observationRegistry;
	
	private static final String DOCTOR_ID_KEY = "doctorId";
	private static final String PRIVATE_PRACTICE_ID_KEY = "privatePracticeId";
	private static final String CLINICAL_ENTITY_ID_KEY = "clinicalEntityId";
	private static final String REQUEST_KEY = "request";

	public MedicalScheduleObservation(MedicalScheduleService medicalScheduleService, ObservationRegistry observationRegistry) {
		this.medicalScheduleService = medicalScheduleService;
		this.observationRegistry = observationRegistry;
	}

	public MedicalSchedule findById(Long id) {
		val observationName = "MedicalScheduleServiceImpl#findById";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue("id", String.valueOf(id))
				.observe(() -> medicalScheduleService.findById(id));
	}
	
	public MedicalScheduleResponse findByPrivPractice(Long doctorId, Long privatePracticeId) {
		val observationName = "MedicalScheduleController#findByPrivPractice";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(DOCTOR_ID_KEY, String.valueOf(doctorId))
				.lowCardinalityKeyValue(PRIVATE_PRACTICE_ID_KEY, String.valueOf(privatePracticeId))
				.observe(() -> medicalScheduleService.findOneByDoctorAndPrivatePractice(doctorId, privatePracticeId));
	}
	
	public MedicalScheduleResponse findByClinicalEntity(Long doctorId, Long clinicalEntityId) {
		val observationName = "MedicalScheduleController#findByClinicalEntity";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(DOCTOR_ID_KEY, String.valueOf(doctorId))
				.lowCardinalityKeyValue(CLINICAL_ENTITY_ID_KEY, String.valueOf(clinicalEntityId))
				.observe(() -> medicalScheduleService.findOneByDoctorAndClinicalEntity(doctorId, clinicalEntityId));
	}
	
	public MedicalScheduleResponse create(MedicalScheduleRequest request) {
		val observationName = "MedicalScheduleController#create";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(REQUEST_KEY, request.toString())
				.observe(() -> medicalScheduleService.create(request));
	}
	
	public MedicalScheduleResponse createFixed(MedicalSchedulePlanningFixedRequest request) {
		val observationName = "MedicalScheduleController#createFixed";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(REQUEST_KEY, request.toString())
				.observe(() -> medicalScheduleService.create(request));
	}
	
	public MedicalScheduleResponse createWeekDay(MedicalSchedulePlanningDayRequest request) {
		val observationName = "MedicalScheduleController#createWeekDay";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(REQUEST_KEY, request.toString())
				.observe(() -> medicalScheduleService.create(request));
	}
	
	public MedicalScheduleResponse updateByPrivatePractice(Long doctorId, Long privatePracticeId, MedicalScheduleRequest request) {
		val observationName = "MedicalScheduleController#updateByPrivatePractice";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(DOCTOR_ID_KEY, String.valueOf(doctorId))
				.lowCardinalityKeyValue(PRIVATE_PRACTICE_ID_KEY, String.valueOf(privatePracticeId))
				.lowCardinalityKeyValue(REQUEST_KEY, request.toString())
				.observe(() -> medicalScheduleService.updateByPrivatePractice(doctorId, privatePracticeId, request));
	}
	
	public MedicalScheduleResponse updateByClinicalEntity(Long doctorId, Long clinicalEntityId, MedicalScheduleRequest request) {
		val observationName = "MedicalScheduleController#updateByClinicalEntity";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(DOCTOR_ID_KEY, String.valueOf(doctorId))
				.lowCardinalityKeyValue(CLINICAL_ENTITY_ID_KEY, String.valueOf(clinicalEntityId))
				.lowCardinalityKeyValue(REQUEST_KEY, request.toString())
				.observe(() -> medicalScheduleService.updateByClinicalEntity(doctorId, clinicalEntityId, request));
	}
	
	public void deleteByPrivatePractice(Long doctorId, Long privatePracticeId) {
		val observationName = "MedicalScheduleController#deleteByPrivatePractice";
		Observation
			.createNotStarted(observationName, observationRegistry)
			.lowCardinalityKeyValue(DOCTOR_ID_KEY, String.valueOf(doctorId))
			.lowCardinalityKeyValue(PRIVATE_PRACTICE_ID_KEY, String.valueOf(privatePracticeId))
			.observe(() -> medicalScheduleService.deleteByPrivatePractice(doctorId, privatePracticeId));
	}
	
	public void deleteByClinicalEntity(Long doctorId, Long clinicalEntityId) {
		val observationName = "MedicalScheduleController#deleteByClinicalEntity";
		Observation
			.createNotStarted(observationName, observationRegistry)
			.lowCardinalityKeyValue(DOCTOR_ID_KEY, String.valueOf(doctorId))
			.lowCardinalityKeyValue(CLINICAL_ENTITY_ID_KEY, String.valueOf(clinicalEntityId))
			.observe(() -> medicalScheduleService.deleteByClinicalEntity(doctorId, clinicalEntityId));
	}

}