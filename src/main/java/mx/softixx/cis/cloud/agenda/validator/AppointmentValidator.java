package mx.softixx.cis.cloud.agenda.validator;

import org.springframework.stereotype.Component;

import lombok.val;
import mx.softixx.cis.cloud.agenda.persistence.model.Appointment;
import mx.softixx.cis.cloud.agenda.persistence.repository.AppointmentRepository;
import mx.softixx.cis.common.agenda.exception.AppointmentAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.AppointmentRescheduleDateException;
import mx.softixx.cis.common.agenda.exception.AppointmentServiceConflictException;
import mx.softixx.cis.common.agenda.exception.AppointmentServiceRequiredException;
import mx.softixx.cis.common.agenda.exception.AppointmentSpecialityConflictException;
import mx.softixx.cis.common.agenda.exception.AppointmentSpecialityRequiredException;
import mx.softixx.cis.common.agenda.payload.AppointmentRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentRescheduleRequest;
import mx.softixx.cis.common.core.datetime.LocalDateUtils;

@Component
public class AppointmentValidator {
	
	private final AppointmentRepository appointmentRepository;
	
	public AppointmentValidator(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}

	public void verifyRequestForCreate(AppointmentRequest request) {
		val dsId = request.getDoctorSpecialityId();
		val ppsId = request.getPrivatePracticeServiceId();
		val dcesid = request.getDoctorClinicalEntitySpecialityId();
		val dceservId = request.getDoctorClinicalEntityServiceId();
		verifyRequest(dsId, ppsId, dcesid, dceservId);
		
		val pid = request.getPersonId();
		val date = request.getAppointmentDate();
		
		if (dsId != null) {
			appointmentRepository.findByPatientAndDateInPrivatePractice(dsId, ppsId, pid, date).ifPresent(x -> {
				throw new AppointmentAlreadyExistsException(dsId, ppsId, dcesid, dceservId, pid, date);
			});
		} else {
			appointmentRepository.findByPatientAndDateInClinicalEntity(dcesid, dceservId, pid, date).ifPresent(x -> {
				throw new AppointmentAlreadyExistsException(dsId, ppsId, dcesid, dceservId, pid, date);
			});
		}
	}
	
	public void verifyRequestForUpdate(AppointmentRequest request) {
		val dsId = request.getDoctorSpecialityId();
		val ppsId = request.getPrivatePracticeServiceId();
		val dcesid = request.getDoctorClinicalEntitySpecialityId();
		val dceservId = request.getDoctorClinicalEntityServiceId();
		verifyRequest(dsId, ppsId, dcesid, dceservId);
	}
	
	public void verifyRequestForReschedule(Appointment appointment, AppointmentRescheduleRequest request) {
		val dsId = request.getDoctorSpecialityId();
		val ppsId = request.getPrivatePracticeServiceId();
		val dcesid = request.getDoctorClinicalEntitySpecialityId();
		val dceservId = request.getDoctorClinicalEntityServiceId();
		verifyRequest(dsId, ppsId, dcesid, dceservId);
		
		val date = request.getAppointmentDate();
		val st = request.getStartTime();
		
		if (dsId != null) {
			appointmentRepository.findByDateAndTimeInPrivatePractice(dsId, ppsId, date, st).ifPresent(x -> {
				throw new AppointmentAlreadyExistsException(dsId, ppsId, dcesid, dceservId, date, st);
			});
		} else {
			appointmentRepository.findByDateAndTimeInClinicalEntity(dcesid, dceservId, date, st).ifPresent(x -> {
				throw new AppointmentAlreadyExistsException(dsId, ppsId, dcesid, dceservId, date, st);
			});
		}
		
		if (LocalDateUtils.isEqual(appointment.getAppointmentDate(), date)) {
			throw new AppointmentRescheduleDateException();
		}
	}
	
	private void verifyRequest(Long dsId, Long ppsId, Long dcesid, Long dceservId) {
		if (dsId == null && dcesid == null) {
			throw new AppointmentSpecialityRequiredException();
		}

		if (dsId != null && dcesid != null) {
			throw new AppointmentSpecialityConflictException();
		}
		
		if (ppsId == null && dceservId == null) {
			throw new AppointmentServiceRequiredException();
		}
		
		if (ppsId != null && dceservId != null) {
			throw new AppointmentServiceConflictException();
		}
	}
	
}