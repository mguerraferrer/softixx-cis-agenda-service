package mx.softixx.cis.cloud.agenda.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.common.agenda.payload.AppointmentConfirmationRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

@Service
public class AppointmentConfirmationServiceImpl implements AppointmentConfirmationService {

	private final AppointmentService appointmentService;
	
	public AppointmentConfirmationServiceImpl(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@Override
	@Transactional
	public AppointmentResponse confirm(Long id, AppointmentConfirmationRequest request) {
		val appointment = appointmentService.findById(id);
		appointment.setConfirmation(request.getConfirmation());
		return appointmentService.saveAndReturn(appointment);
	}

}