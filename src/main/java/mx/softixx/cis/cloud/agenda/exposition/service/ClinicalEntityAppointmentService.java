package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.List;

import mx.softixx.cis.common.agenda.payload.AppointmentFindByBaseRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentFindByPersonRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

public interface ClinicalEntityAppointmentService {
	
	AppointmentResponse findByPatient(AppointmentFindByPersonRequest request);
	
	List<AppointmentResponse> findAll(AppointmentFindByBaseRequest request);
	
}