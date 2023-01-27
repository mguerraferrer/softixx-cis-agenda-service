package mx.softixx.cis.cloud.agenda.exposition.service;

import mx.softixx.cis.common.agenda.payload.AppointmentConfirmationRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

public interface AppointmentConfirmationService {
	
	AppointmentResponse confirm(Long id, AppointmentConfirmationRequest request);
	
}