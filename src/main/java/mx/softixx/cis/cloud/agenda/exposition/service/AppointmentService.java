package mx.softixx.cis.cloud.agenda.exposition.service;

import mx.softixx.cis.cloud.agenda.persistence.model.Appointment;
import mx.softixx.cis.common.agenda.payload.AppointmentRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

public interface AppointmentService {
	
	Appointment findById(Long id);
	
	AppointmentResponse findOne(Long id);
	
	AppointmentResponse findByFolio(String folio);
	
	AppointmentResponse create(AppointmentRequest request);
	
	AppointmentResponse update(Long id, AppointmentRequest request);
	
	AppointmentResponse saveAndReturn(Appointment appointment);
	
	Appointment save(Appointment appointment);
	
	void deleteById(Long id);
	
}