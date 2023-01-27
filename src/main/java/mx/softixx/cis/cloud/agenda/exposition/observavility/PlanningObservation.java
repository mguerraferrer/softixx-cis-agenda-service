package mx.softixx.cis.cloud.agenda.exposition.observavility;

import java.util.List;

import org.springframework.stereotype.Component;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.service.PlanningService;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.common.agenda.payload.PlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.PlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.PlanningRequest;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;

@Component
public class PlanningObservation {

	private final PlanningService planningService;
	private final ObservationRegistry observationRegistry;
	
	private static final String MEDICAL_SCHEDULE_ID_KEY = "medicalScheduleId";
	private static final String PLANNING_ID_KEY = "planningId";
	private static final String PLANNING_REQUEST_KEY = "planningRequest";
	private static final String OBSERVATION_UPDATE_KEY = "PlanningController#update";

	public PlanningObservation(PlanningService planningService, ObservationRegistry observationRegistry) {
		this.planningService = planningService;
		this.observationRegistry = observationRegistry;
	}

	public List<PlanningResponse> findByMedicalSchedule(MedicalSchedule medicalSchedule) {
		val observationName = "PlanningController#findByMedicalSchedule";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
				.observe(() -> planningService.findByMedicalSchedule(medicalSchedule));
	}
	
	public PlanningResponse create(MedicalSchedule medicalSchedule, PlanningFixedRequest planningFixedRequest) {
		val observationName = "PlanningController#create";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
				.lowCardinalityKeyValue(PLANNING_REQUEST_KEY, planningFixedRequest.toString())
				.observe(() -> planningService.create(medicalSchedule, planningFixedRequest));
	}
	
	public PlanningResponse create(MedicalSchedule medicalSchedule, PlanningDayRequest planningDayRequest) {
		val observationName = "PlanningController#create";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
				.lowCardinalityKeyValue(PLANNING_REQUEST_KEY, planningDayRequest.toString())
				.observe(() -> planningService.create(medicalSchedule, planningDayRequest));
	}
	
	public PlanningResponse update(Long planningId, PlanningRequest planningRequest) {
		val observationName = OBSERVATION_UPDATE_KEY;
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(PLANNING_ID_KEY, String.valueOf(planningId))
				.lowCardinalityKeyValue(PLANNING_REQUEST_KEY, planningRequest.toString())
				.observe(() -> planningService.update(planningId, planningRequest));
	}
	
	public PlanningResponse update(Long planningId, PlanningFixedRequest planningFixedRequest) {
		val observationName = OBSERVATION_UPDATE_KEY;
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(PLANNING_ID_KEY, String.valueOf(planningId))
				.lowCardinalityKeyValue(PLANNING_REQUEST_KEY, planningFixedRequest.toString())
				.observe(() -> planningService.update(planningId, planningFixedRequest));
	}
	
	public PlanningResponse update(Long planningId, PlanningDayRequest planningDayRequest) {
		val observationName = OBSERVATION_UPDATE_KEY;
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(PLANNING_ID_KEY, String.valueOf(planningId))
				.lowCardinalityKeyValue(PLANNING_REQUEST_KEY, planningDayRequest.toString())
				.observe(() -> planningService.update(planningId, planningDayRequest));
	}
	
	public void deleteByMedicalSchedule(MedicalSchedule medicalSchedule) {
		val observationName = "PlanningController#deleteByMedicalSchedule";
		Observation
			.createNotStarted(observationName, observationRegistry)
			.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
			.observe(() -> planningService.delete(medicalSchedule));
	}
	
	public void deleteById(Long planningId) {
		val observationName = "PlanningController#deleteById";
		Observation
		.createNotStarted(observationName, observationRegistry)
		.lowCardinalityKeyValue(PLANNING_ID_KEY, String.valueOf(planningId))
		.observe(() -> planningService.delete(planningId));
	}

}