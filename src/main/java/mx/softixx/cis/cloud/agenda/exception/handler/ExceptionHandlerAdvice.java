package mx.softixx.cis.cloud.agenda.exception.handler;

import java.sql.SQLException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.val;
import mx.softixx.cis.common.agenda.exception.AppointmentAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.AppointmentCloneFrequencyException;
import mx.softixx.cis.common.agenda.exception.AppointmentNotFoundException;
import mx.softixx.cis.common.agenda.exception.AppointmentRescheduleDateException;
import mx.softixx.cis.common.agenda.exception.AppointmentServiceConflictException;
import mx.softixx.cis.common.agenda.exception.AppointmentServiceRequiredException;
import mx.softixx.cis.common.agenda.exception.AppointmentSpecialityConflictException;
import mx.softixx.cis.common.agenda.exception.AppointmentSpecialityRequiredException;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleHealthcareCenterConflictException;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleHealthcareCenterRequiredException;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleNotFoundException;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayConflictException;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayDuplicateException;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayNotFoundException;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayOutOfRangeException;
import mx.softixx.cis.common.agenda.exception.NonWorkingDayRequiredException;
import mx.softixx.cis.common.agenda.exception.PlanningAppointmentDurationException;
import mx.softixx.cis.common.agenda.exception.PlanningClinicalEntityRequiredException;
import mx.softixx.cis.common.agenda.exception.PlanningDayAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.PlanningDayFixedException;
import mx.softixx.cis.common.agenda.exception.PlanningDayNotFoundException;
import mx.softixx.cis.common.agenda.exception.PlanningDayRepeatException;
import mx.softixx.cis.common.agenda.exception.PlanningDoctorClinicalEntitySpecialityAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.PlanningDoctorClinicalEntitySpecialityRequiredException;
import mx.softixx.cis.common.agenda.exception.PlanningDoctorSpecialityAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.PlanningDoctorSpecialityRequiredException;
import mx.softixx.cis.common.agenda.exception.PlanningFixedConflictException;
import mx.softixx.cis.common.agenda.exception.PlanningFixedException;
import mx.softixx.cis.common.agenda.exception.PlanningNotFoundException;
import mx.softixx.cis.common.agenda.exception.PlanningSpecialityConflictException;
import mx.softixx.cis.common.agenda.exception.PlanningSpecialityRequiredException;
import mx.softixx.cis.common.agenda.exception.PlanningWeekDayRequiredException;
import mx.softixx.cis.common.validation.exception.CustomException;
import mx.softixx.cis.common.validation.util.ProblemDetailUtils;

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		if (e.getCause().getCause() instanceof SQLException ex) {
			System.out.println(String.format("SQLException message: %s", ex.getMessage()));
		}
		return ProblemDetailUtils.conflict(CustomException.populateProperties("SQLException"));
	}

	@ExceptionHandler(MedicalScheduleAlreadyExistsException.class)
	public ProblemDetail handleMedicalScheduleAlreadyExistsException(MedicalScheduleAlreadyExistsException e) {
		val properties = CustomException.populateProperties(e);
		properties.put("doctorId", e.getDoctorId());
		properties.put("privatePracticeId", e.getPrivatePracticeId());
		properties.put("clinicalEntityId", e.getClinicalEntityId());

		return ProblemDetailUtils.preconditionFailed(e.getMessage(), properties);
	}

	@ExceptionHandler(MedicalScheduleHealthcareCenterConflictException.class)
	public ProblemDetail handleMedicalScheduleHealthcareCenterConflictException(
			MedicalScheduleHealthcareCenterConflictException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(MedicalScheduleHealthcareCenterRequiredException.class)
	public ProblemDetail handleMedicalScheduleHealthcareCenterRequiredException(
			MedicalScheduleHealthcareCenterRequiredException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(MedicalScheduleNotFoundException.class)
	public ProblemDetail handleMedicalScheduleNotFoundException(MedicalScheduleNotFoundException e) {
		val properties = CustomException.populateProperties(e);
		properties.put("id", e.getId());
		properties.put("doctorId", e.getDoctorId());
		properties.put("privatePracticeId", e.getPrivatePracticeId());
		properties.put("clinicalEntityId", e.getClinicalEntityId());

		return ProblemDetailUtils.notFound(e.getMessage(), properties);
	}

	@ExceptionHandler(PlanningAppointmentDurationException.class)
	public ProblemDetail handlePlanningAppointmentDurationException(PlanningAppointmentDurationException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningClinicalEntityRequiredException.class)
	public ProblemDetail handlePlanningClinicalEntityRequiredException(PlanningClinicalEntityRequiredException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningDayAlreadyExistsException.class)
	public ProblemDetail handlePlanningDayAlreadyExistsException(PlanningDayAlreadyExistsException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningDayNotFoundException.class)
	public ProblemDetail handlePlanningDayNotFoundException(PlanningDayNotFoundException e) {
		val properties = CustomException.populateProperties(e);
		properties.put("id", e.getId());

		return ProblemDetailUtils.notFound(e.getMessage(), properties);
	}

	@ExceptionHandler(PlanningDoctorSpecialityAlreadyExistsException.class)
	public ProblemDetail handlePlanningDoctorSpecialityAlreadyExistsException(
			PlanningDoctorSpecialityAlreadyExistsException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningDoctorClinicalEntitySpecialityAlreadyExistsException.class)
	public ProblemDetail handlePlanningDoctorClinicalEntitySpecialityAlreadyExistsException(
			PlanningDoctorClinicalEntitySpecialityAlreadyExistsException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningDoctorSpecialityRequiredException.class)
	public ProblemDetail handlePlanningDoctorSpecialityRequiredException(PlanningDoctorSpecialityRequiredException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningDoctorClinicalEntitySpecialityRequiredException.class)
	public ProblemDetail handlePlanningDoctorClinicalEntitySpecialityRequiredException(
			PlanningDoctorClinicalEntitySpecialityRequiredException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningNotFoundException.class)
	public ProblemDetail handlePlanningNotFoundException(PlanningNotFoundException e) {
		val properties = CustomException.populateProperties(e);
		properties.put("id", e.getId());
		properties.put("medicalScheduleId", e.getMsId());
		properties.put("doctorSpecialityId", e.getDsId());
		properties.put("doctorClinicalEntitySpecialityId", e.getDcesId());
		properties.put("day", e.getDay());

		return ProblemDetailUtils.notFound(e.getMessage(), properties);
	}

	@ExceptionHandler(PlanningSpecialityConflictException.class)
	public ProblemDetail handlePlanningSpecialityConflictException(PlanningSpecialityConflictException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningSpecialityRequiredException.class)
	public ProblemDetail handlePlanningSpecialityRequiredException(PlanningSpecialityRequiredException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningWeekDayRequiredException.class)
	public ProblemDetail handlePlanningWeekDayRequiredException(PlanningWeekDayRequiredException e) {
		return ProblemDetailUtils.preconditionFailed(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningDayRepeatException.class)
	public ProblemDetail handlePlanningDayRepeatException(PlanningDayRepeatException e) {
		return ProblemDetailUtils.badRequest(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningDayFixedException.class)
	public ProblemDetail handlePlanningDayFixedException(PlanningDayFixedException e) {
		return ProblemDetailUtils.badRequest(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningFixedException.class)
	public ProblemDetail handlePlanningFixedException(PlanningFixedException e) {
		return ProblemDetailUtils.badRequest(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(PlanningFixedConflictException.class)
	public ProblemDetail handlePlanningFixedConflictException(PlanningFixedConflictException e) {
		return ProblemDetailUtils.badRequest(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler({ NonWorkingDayNotFoundException.class, NonWorkingDayRequiredException.class,
			NonWorkingDayConflictException.class, NonWorkingDayOutOfRangeException.class,
			NonWorkingDayDuplicateException.class })
	public ProblemDetail handleNonWorkingDayExceptions(CustomException e) {
		if (e instanceof NonWorkingDayNotFoundException ex) {
			val properties = CustomException.populateProperties(ex);
			properties.put("id", ex.getId());
			properties.put("nwd", ex.getNwd());

			return ProblemDetailUtils.notFound(e.getMessage(), properties);
		}

		return ProblemDetailUtils.badRequest(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(AppointmentNotFoundException.class)
	public ProblemDetail handleAppointmentNotFoundException(AppointmentNotFoundException e) {
		val properties = CustomException.populateProperties(e);
		properties.put("id", e.getId());
		properties.put("folio", e.getFolio());
		properties.put("dsId", e.getDsId());
		properties.put("dcesId", e.getDcesId());
		properties.put("personId", e.getPersonId());
		properties.put("date", e.getDate());

		return ProblemDetailUtils.notFound(e.getMessage(), properties);
	}

	@ExceptionHandler({ AppointmentSpecialityConflictException.class, AppointmentSpecialityRequiredException.class,
						AppointmentServiceConflictException.class, AppointmentServiceRequiredException.class,
						AppointmentRescheduleDateException.class, AppointmentCloneFrequencyException.class })
	public ProblemDetail handleAppointmentSpecialityAndServiceException(CustomException e) {
		return ProblemDetailUtils.badRequest(e.getMessage(), CustomException.populateProperties(e));
	}

	@ExceptionHandler(AppointmentAlreadyExistsException.class)
	public ProblemDetail handleAppointmentAlreadyExistsException(AppointmentAlreadyExistsException e) {
		val properties = CustomException.populateProperties(e);
		properties.put("doctorSpecialityId", e.getDoctorSpecialityId());
		properties.put("doctorClinicalEntitySpecialityId", e.getDoctorClinicalEntitySpecialityId());
		properties.put("personId", e.getPersonId());
		properties.put("date", e.getDate());

		return ProblemDetailUtils.preconditionFailed(e.getMessage(), properties);
	}

}