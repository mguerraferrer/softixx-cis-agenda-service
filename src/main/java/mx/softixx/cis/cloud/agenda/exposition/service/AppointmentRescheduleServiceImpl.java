package mx.softixx.cis.cloud.agenda.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.AppointmentMapper;
import mx.softixx.cis.cloud.agenda.validator.AppointmentValidator;
import mx.softixx.cis.common.agenda.payload.AppointmentRescheduleRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;
import mx.softixx.cis.common.agenda.payload.AppointmentStatus;

@Service
public class AppointmentRescheduleServiceImpl implements AppointmentRescheduleService {

	private final AppointmentValidator appointmentValidator;
	private final AppointmentService appointmentService;

	public AppointmentRescheduleServiceImpl(AppointmentValidator appointmentValidator,
											 AppointmentService appointmentService) {
		this.appointmentValidator = appointmentValidator;
		this.appointmentService = appointmentService;
	}

	@Override
	@Transactional
	public AppointmentResponse reschedule(Long id, AppointmentRescheduleRequest request) {
		val appointment = appointmentService.findById(id);
		appointmentValidator.verifyRequestForReschedule(appointment, request);
		
		appointment.setStatus(AppointmentStatus.RESCHEDULED);
		appointment.setRescheduledBy(request.getRescheduledBy());
		appointmentService.save(appointment);

		val appointmentRescheduled = AppointmentMapper.map(appointment, request);
		return appointmentService.saveAndReturn(appointmentRescheduled);
	}

}