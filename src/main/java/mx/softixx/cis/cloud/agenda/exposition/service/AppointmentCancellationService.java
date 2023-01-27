package mx.softixx.cis.cloud.agenda.exposition.service;

import mx.softixx.cis.common.agenda.payload.AppointmentCancellationRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

public interface AppointmentCancellationService {
	
	AppointmentResponse cancel(Long id, AppointmentCancellationRequest request);
	
}