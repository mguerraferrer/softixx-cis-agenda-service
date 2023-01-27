package mx.softixx.cis.cloud.agenda.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.validator.AppointmentCloneValidator;
import mx.softixx.cis.common.agenda.payload.AppointmentCloneRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentCloneResponse;

@Service
public class AppointmentCloneServiceImpl implements AppointmentCloneService {

	private final AppointmentCloneValidator appointmentCloneValidator;
	private final AppointmentService appointmentService;

	public AppointmentCloneServiceImpl(AppointmentCloneValidator appointmentCloneValidator,
									   AppointmentService appointmentService) {
		this.appointmentCloneValidator = appointmentCloneValidator;
		this.appointmentService = appointmentService;
	}
	
	@Override
	@Transactional
	public AppointmentCloneResponse clone(Long id, AppointmentCloneRequest request) {
		val appointment = appointmentService.findById(id);
		appointmentCloneValidator.verifyRequest(appointment, request);
		
		return null;
	}

}