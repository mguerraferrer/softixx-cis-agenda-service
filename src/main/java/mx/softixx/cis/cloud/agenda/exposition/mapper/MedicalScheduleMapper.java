package mx.softixx.cis.cloud.agenda.exposition.mapper;

import java.util.List;

import lombok.val;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.NonWorkingDay;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleResponse;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayRequest;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;
import mx.softixx.cis.common.core.datetime.LocalDateUtils;

public final class MedicalScheduleMapper {
	
	private MedicalScheduleMapper() {		
	}
	
	public static MedicalScheduleResponse map(MedicalSchedule medicalSchedule) {
		if (medicalSchedule == null) {
			return null;
		}
		
		return MedicalScheduleResponse
				.builder()
				.id(medicalSchedule.getId())
				.doctorId(medicalSchedule.getDoctorId())
				.privatePracticeId(medicalSchedule.getPrivatePracticeId())
				.clinicalEntityId(medicalSchedule.getClinicalEntityId())
				.endDate(medicalSchedule.getEndDate())
				.plannings(PlanningMapper.map(medicalSchedule.getPlannings()))
				.active(medicalSchedule.isActive())
				.build();
	}
	
	public static MedicalScheduleResponse map(MedicalSchedule medicalSchedule, List<PlanningResponse> plannings) {
		if (medicalSchedule == null) {
			return null;
		}
		
		return MedicalScheduleResponse
				.builder()
				.id(medicalSchedule.getId())
				.doctorId(medicalSchedule.getDoctorId())
				.privatePracticeId(medicalSchedule.getPrivatePracticeId())
				.clinicalEntityId(medicalSchedule.getClinicalEntityId())
				.endDate(medicalSchedule.getEndDate())
				.plannings(plannings)
				.active(medicalSchedule.isActive())
				.build();
	}
	
	public static MedicalSchedule map(MedicalScheduleRequest request) {
		if (request == null) {
			return null;
		}

		val doctorId = request.getDoctorId();
		val privatePracticeId = request.getPrivatePracticeId();
		val clinicalEntityId = request.getClinicalEntityId();
		val endDate = request.getEndDate();

		val medicalSchedule = new MedicalSchedule();
		if (privatePracticeId != null) {
			return medicalSchedule.withPrivatePractice(doctorId, privatePracticeId, endDate);
		} else if (clinicalEntityId != null) {
			return medicalSchedule.withClinicalEntity(doctorId, clinicalEntityId, endDate);
		}
		return null;
	}
	
	public static MedicalSchedule map(MedicalSchedule medicalSchedule, MedicalScheduleRequest request) {
		if (medicalSchedule == null || request == null) {
			return null;
		}

		medicalSchedule.setEndDate(request.getEndDate());
		medicalSchedule.setActive(LocalDateUtils.isFutureOrPresent(request.getEndDate()));
		return medicalSchedule;
	}
	
	public static void map(final MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest) {
		val source = nonWorkingDayRequest.nonWorkingDaysStream().toList();
		if (medicalSchedule.getNonWorkingDays().isEmpty()) {
			source.forEach(medicalSchedule::addNonWorkingDays);
		} else {
			val nonWorkingDays = source.stream().map(nwd -> new NonWorkingDay(medicalSchedule, nwd)).toList();
			medicalSchedule.addNonWorkingDays(nonWorkingDays);
		}
	}
	
}