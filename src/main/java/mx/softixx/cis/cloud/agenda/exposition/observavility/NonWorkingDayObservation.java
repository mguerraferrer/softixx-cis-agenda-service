package mx.softixx.cis.cloud.agenda.exposition.observavility;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.service.NonWorkingDayService;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayRequest;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse.NwdResponse;

@Component
public class NonWorkingDayObservation {

	private final NonWorkingDayService nonWorkingDayService;
	private final ObservationRegistry observationRegistry;
	
	private static final String MEDICAL_SCHEDULE_ID_KEY = "medicalScheduleId";
	private static final String NWD_REQUEST_KEY = "nwdRequest";

	public NonWorkingDayObservation(NonWorkingDayService nonWorkingDayService, ObservationRegistry observationRegistry) {
		this.nonWorkingDayService = nonWorkingDayService;
		this.observationRegistry = observationRegistry;
	}

	public NonWorkingDayResponse findByMedicalSchedule(MedicalSchedule medicalSchedule) {
		val observationName = "NonWorkingDayController#findByMedicalSchedule";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
				.observe(() -> nonWorkingDayService.findByMedicalSchedule(medicalSchedule));
	}
	
	public NwdResponse findOne(MedicalSchedule medicalSchedule, LocalDate nwd) {
		val observationName = "NonWorkingDayController#findOne";
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
				.lowCardinalityKeyValue("nwd", nwd.toString())
				.observe(() -> nonWorkingDayService.findOne(medicalSchedule, nwd));
	}
	
	public NonWorkingDayResponse add(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		val observationName = "NonWorkingDayController#add";
		return addOrUpdate(medicalSchedule, nonWorkingDayRequest, observationName);
	}
	
	public NonWorkingDayResponse update(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		val observationName = "NonWorkingDayController#update";
		return addOrUpdate(medicalSchedule, nonWorkingDayRequest, observationName);
	}

	public void delete(MedicalSchedule medicalSchedule) {
		val observationName = "NonWorkingDayController#delete";
		Observation
			.createNotStarted(observationName, observationRegistry)
			.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
			.observe(() -> nonWorkingDayService.delete(medicalSchedule));
	}
	
	public void bulkDelete(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		val observationName = "NonWorkingDayController#bulkDelete";
		Observation
			.createNotStarted(observationName, observationRegistry)
			.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
			.lowCardinalityKeyValue(NWD_REQUEST_KEY, nonWorkingDayRequest.toString())
			.observe(() -> nonWorkingDayService.delete(medicalSchedule, nonWorkingDayRequest));
	}
	
	public void deleteByNwd(MedicalSchedule medicalSchedule, LocalDate nwd) {
		val observationName = "NonWorkingDayController#deleteByNwd";
		Observation
		.createNotStarted(observationName, observationRegistry)
		.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
		.lowCardinalityKeyValue("nwd", nwd.toString())
		.observe(() -> nonWorkingDayService.delete(medicalSchedule, nwd));
	}
	
	public void deleteById(Long nwdId) {
		val observationName = "NonWorkingDayController#deleteById";
		Observation
			.createNotStarted(observationName, observationRegistry)
			.lowCardinalityKeyValue("nwdId", String.valueOf(nwdId))
			.observe(() -> nonWorkingDayService.deleteById(nwdId));
	}
	
	private NonWorkingDayResponse addOrUpdate(MedicalSchedule medicalSchedule,
											  NonWorkingDayRequest nonWorkingDayRequest, 
											  String observationName) {
		return Observation
				.createNotStarted(observationName, observationRegistry)
				.lowCardinalityKeyValue(MEDICAL_SCHEDULE_ID_KEY, String.valueOf(medicalSchedule.getId()))
				.lowCardinalityKeyValue(NWD_REQUEST_KEY, nonWorkingDayRequest.toString())
				.observe(() -> nonWorkingDayService.addOrUpdate(medicalSchedule, nonWorkingDayRequest));
	}

}