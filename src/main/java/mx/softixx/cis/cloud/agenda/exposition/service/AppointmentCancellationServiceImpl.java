package mx.softixx.cis.cloud.agenda.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.common.agenda.payload.AppointmentCancellationRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

@Service
public class AppointmentCancellationServiceImpl implements AppointmentCancellationService {

	private final AppointmentService appointmentService;

	public AppointmentCancellationServiceImpl(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@Override
	@Transactional
	public AppointmentResponse cancel(Long id, AppointmentCancellationRequest request) {
		val appointment = appointmentService.findById(id);
		appointment.setStatus(AppointmentCancellationRequest.status);
		appointment.setCancelledBy(request.getCancelledBy());
		return appointmentService.saveAndReturn(appointment);
	}

}