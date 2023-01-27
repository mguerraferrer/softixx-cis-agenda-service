package mx.softixx.cis.cloud.agenda.exposition.service;

import mx.softixx.cis.common.agenda.payload.AppointmentCloneRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentCloneResponse;

public interface AppointmentCloneService {
	
	AppointmentCloneResponse clone(Long id, AppointmentCloneRequest request);
	
}