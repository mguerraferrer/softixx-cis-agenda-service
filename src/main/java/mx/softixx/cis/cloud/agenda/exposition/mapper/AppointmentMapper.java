package mx.softixx.cis.cloud.agenda.exposition.mapper;

import lombok.val;
import mx.softixx.cis.cloud.agenda.persistence.model.Appointment;
import mx.softixx.cis.common.agenda.payload.AppointmentConfirmation;
import mx.softixx.cis.common.agenda.payload.AppointmentRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentRescheduleRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;
import mx.softixx.cis.common.agenda.payload.AppointmentStatus;

public final class AppointmentMapper {

	private AppointmentMapper() {
	}

	public static AppointmentResponse map(Appointment appointment) {
		if (appointment == null) {
			return null;
		}
		
		return AppointmentResponse
				.builder()
				.id(appointment.getId())
				.personId(appointment.getPersonId())
				.doctorSpecialityId(appointment.getDoctorSpecialityId())
				.privatePracticeServiceId(appointment.getPrivatePracticeServiceId())
				.doctorClinicalEntitySpecialityId(appointment.getDoctorClinicalEntitySpecialityId())
				.doctorClinicalEntityServiceId(appointment.getDoctorClinicalEntityServiceId())
				.folio(appointment.getFolio())
				.type(appointment.getType())
				.origin(appointment.getOrigin())
				.status(appointment.getStatus())
				.confirmation(appointment.getConfirmation())
				.appointmentDate(appointment.getAppointmentDate())
				.startTime(appointment.getStartTime())
				.endTime(appointment.getEndTime())
				.month(appointment.getMonth())
				.originalAppointment(appointment.getOriginalAppointmentId())
				.anotherPersonName(appointment.getAnotherPersonName())
				.relationshipId(appointment.getRelationshipId())
				.cancelledBy(appointment.getCancelledBy())
				.rescheduledBy(appointment.getRescheduledBy())
				.additionalInfo(appointment.getAdditionalInfo())
				.build();
	}

	public static Appointment map(AppointmentRequest request) {
		if (request == null) {
			return null;
		}

		val appointment = new Appointment(request.getDoctorSpecialityId(), request.getPrivatePracticeServiceId(),
				request.getDoctorClinicalEntitySpecialityId(), request.getDoctorClinicalEntityServiceId(),
				request.getPersonId(), request.getAppointmentDate(), request.getStartTime(), request.getEndTime());

		appointment.setType(request.getType());
		appointment.setOrigin(request.getOrigin());
		appointment.setStatus(AppointmentStatus.CREATED);
		appointment.setConfirmation(AppointmentConfirmation.UNCONFIRMED);
		appointment.setRelationshipId(request.getRelationshipId());
		appointment.setAnotherPersonName(request.getAnotherPersonName());
		appointment.setAdditionalInfo(request.getAdditionalInfo());
		return appointment;
	}
	
	public static void map(Appointment appointment, AppointmentRequest request) {
		if (appointment != null && request != null) {
			populateSpecialityAndService(appointment, request);
			populateTypeAndOrigin(appointment, request);
			populateStatus(appointment, request);
			populateDateAndTime(appointment, request);
			
			appointment.setAdditionalInfo(request.getAdditionalInfo());
		}
	}
	
	public static Appointment map(Appointment appointment, AppointmentRescheduleRequest request) {
		if (appointment != null && request != null) {
			val reschedule = new Appointment(request.getDoctorSpecialityId(), request.getPrivatePracticeServiceId(),
					request.getDoctorClinicalEntitySpecialityId(), request.getDoctorClinicalEntityServiceId(),
					appointment.getPersonId(), request.getAppointmentDate(), request.getStartTime(),
					request.getEndTime());
			reschedule.setOriginalAppointment(appointment);
			reschedule.setType(appointment.getType());
			reschedule.setOrigin(appointment.getOrigin());
			reschedule.setStatus(AppointmentStatus.CREATED);
			reschedule.setConfirmation(AppointmentConfirmation.UNCONFIRMED);
			return reschedule;
		}
		return null;
	}

	private static void populateSpecialityAndService(Appointment appointment, AppointmentRequest request) {
		if (request.getDoctorSpecialityId() != null) {
			appointment.setDoctorSpecialityId(request.getDoctorSpecialityId());
		}
		
		if (request.getPrivatePracticeServiceId() != null) {
			appointment.setPrivatePracticeServiceId(request.getPrivatePracticeServiceId());
		}
		
		if (request.getDoctorClinicalEntitySpecialityId() != null) {
			appointment.setDoctorClinicalEntitySpecialityId(request.getDoctorClinicalEntitySpecialityId());
		}
		
		if (request.getDoctorClinicalEntityServiceId() != null) {
			appointment.setDoctorClinicalEntityServiceId(request.getDoctorClinicalEntityServiceId());
		}
	}
	
	private static void populateTypeAndOrigin(Appointment appointment, AppointmentRequest request) {
		if (request.getType() != null) {
			appointment.setType(appointment.getType());
		}
		
		if (request.getOrigin() != null) {
			appointment.setOrigin(request.getOrigin());
		}
			
		if (request.getRelationshipId() != null) {
			appointment.setRelationshipId(request.getRelationshipId());
		}
		
		if (request.getAnotherPersonName() != null) {
			appointment.setAnotherPersonName(request.getAnotherPersonName());
		}
	}
	
	private static void populateStatus(Appointment appointment, AppointmentRequest request) {
		appointment.setStatus(request.getStatus());
		appointment.setCancelledBy(request.getCancelledBy());
		appointment.setRescheduledBy(request.getRescheduledBy());
	}
	
	private static void populateDateAndTime(Appointment appointment, AppointmentRequest request) {
		appointment.setAppointmentDate(request.getAppointmentDate());
		
		if (request.getStartTime() != null) {
			appointment.setStartTime(request.getStartTime());
		}
		
		if (request.getEndTime() != null) {
			appointment.setEndTime(request.getEndTime());
		}
	}

}