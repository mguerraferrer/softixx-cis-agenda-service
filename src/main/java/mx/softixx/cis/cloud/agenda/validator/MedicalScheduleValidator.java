package mx.softixx.cis.cloud.agenda.validator;

import lombok.val;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleHealthcareCenterConflictException;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleHealthcareCenterRequiredException;
import mx.softixx.cis.common.agenda.exception.PlanningDayFixedException;
import mx.softixx.cis.common.agenda.exception.PlanningDayRepeatException;
import mx.softixx.cis.common.agenda.exception.PlanningSpecialityConflictException;
import mx.softixx.cis.common.agenda.exception.PlanningSpecialityRequiredException;
import mx.softixx.cis.common.agenda.payload.DayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleRequest;
import mx.softixx.cis.common.agenda.payload.PlanningRequest;
import mx.softixx.cis.common.core.collection.ListUtils;

public final class MedicalScheduleValidator {

	private MedicalScheduleValidator() {
	}

	public static void verifyRequest(MedicalSchedulePlanningRequest request) {
		val medicalScheduleRequest = request.getMedicalSchedule();
		val planningRequest = request.getPlanning();
		verifyRequest(medicalScheduleRequest, planningRequest);
		
		if (request instanceof MedicalSchedulePlanningFixedRequest planningFixedRequest) {
			verifyRequest(planningFixedRequest);
		} else if (request instanceof MedicalSchedulePlanningDayRequest planningDayRequest) {
			verifyRequest(planningDayRequest);
		}
	}
	
	private static void verifyRequest(MedicalScheduleRequest medicalScheduleRequest, PlanningRequest planningRequest) {
		verifyRequest(medicalScheduleRequest);
		verifyRequest(planningRequest);
	}
	
	private static void verifyRequest(MedicalScheduleRequest medicalScheduleRequest) {
		val privatePracticeId = medicalScheduleRequest.getPrivatePracticeId();
		val clinicalEntityId = medicalScheduleRequest.getClinicalEntityId();
		
		if (privatePracticeId == null && clinicalEntityId == null) {
			throw new MedicalScheduleHealthcareCenterRequiredException();
		}

		if (privatePracticeId != null && clinicalEntityId != null) {
			throw new MedicalScheduleHealthcareCenterConflictException();
		}
	}
	
	public static void verifyRequest(PlanningRequest planningRequest) {
		val doctorSpecialityId = planningRequest.getDoctorSpecialityId();
		val doctorClinicalEntitySpecialityId = planningRequest.getDoctorClinicalEntitySpecialityId();

		if (doctorSpecialityId == null && doctorClinicalEntitySpecialityId == null) {
			throw new PlanningSpecialityRequiredException();
		}

		if (doctorSpecialityId != null && doctorClinicalEntitySpecialityId != null) {
			throw new PlanningSpecialityConflictException();
		}

		AppointmentDurationValidator.validate(planningRequest.getAppointmentDuration());
	}
	
	private static void verifyRequest(MedicalSchedulePlanningFixedRequest planningDayRequest) {
		val planningRequest = planningDayRequest.getPlanning();
		if (!planningRequest.isFixedSchedule()) {
			throw new PlanningDayFixedException();
		}
	}
	
	private static void verifyRequest(MedicalSchedulePlanningDayRequest planningDayRequest) {
		val planningDays = planningDayRequest.getPlanningDays().stream().map(DayRequest::getDay).toList();
		val hasDuplicateDays = ListUtils.hasDuplicateValues(planningDays);
		if (hasDuplicateDays) {
			throw new PlanningDayRepeatException();
		}
		
		val planningRequest = planningDayRequest.getPlanning();
		if (planningRequest.isFixedSchedule()) {
			throw new PlanningDayFixedException();
		}
	}

}