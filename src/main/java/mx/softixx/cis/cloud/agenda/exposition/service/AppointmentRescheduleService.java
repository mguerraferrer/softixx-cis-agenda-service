package mx.softixx.cis.cloud.agenda.exposition.service;

import mx.softixx.cis.common.agenda.payload.AppointmentRescheduleRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

public interface AppointmentRescheduleService {
	
	AppointmentResponse reschedule(Long id, AppointmentRescheduleRequest request);
	
}