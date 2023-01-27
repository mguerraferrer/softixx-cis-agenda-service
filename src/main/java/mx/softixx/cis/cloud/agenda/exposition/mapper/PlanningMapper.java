package mx.softixx.cis.cloud.agenda.exposition.mapper;

import java.util.Collections;
import java.util.List;

import lombok.val;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningFixed;
import mx.softixx.cis.common.agenda.payload.DayRequest;
import mx.softixx.cis.common.agenda.payload.PlanningRequest;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;

public final class PlanningMapper {

	private PlanningMapper() {		
	}
	
	public static PlanningResponse map(Planning planning, PlanningFixed planningFixed) {
		if (planning == null) {
			return null;
		}
		
		return PlanningResponse
				.builder()
				.id(planning.getId())
				.medicalScheduleId(planning.getMedicalSchedule().getId())
				.doctorClinicalEntitySpecialityId(planning.getDoctorClinicalEntitySpecialityId())
				.doctorSpecialityId(planning.getDoctorSpecialityId())
				.agendaVisualization(planning.getAgendaVisualization())
				.appointmentDuration(planning.getAppointmentDuration())
				.fixedSchedule(planning.isFixedSchedule())
				.planningFixed(PlanningFixedMapper.map(planningFixed))
				.build();
	}
	
	public static PlanningResponse map(Planning planning) {
		if (planning == null) {
			return null;
		}
		
		return PlanningResponse
				.builder()
				.id(planning.getId())
				.medicalScheduleId(planning.getMedicalSchedule().getId())
				.doctorClinicalEntitySpecialityId(planning.getDoctorClinicalEntitySpecialityId())
				.doctorSpecialityId(planning.getDoctorSpecialityId())
				.agendaVisualization(planning.getAgendaVisualization())
				.appointmentDuration(planning.getAppointmentDuration())
				.fixedSchedule(planning.isFixedSchedule())
				.planningDays(PlanningDayMapper.map(planning.getPlanningDays()))
				.build();
	}
	
	public static List<PlanningResponse> map(List<Planning> plannings) {
		if (plannings != null && !plannings.isEmpty()) {
			return plannings.stream().map(PlanningMapper::map).toList();
		}
		return Collections.emptyList();
	}
	
	public static Planning map(MedicalSchedule medicalSchedule, PlanningRequest request) {
		if (medicalSchedule == null || request == null) {
			return null;
		}
		
		val planning = new Planning(medicalSchedule);
		planning.setDoctorSpecialityId(request.getDoctorSpecialityId());
		planning.setDoctorClinicalEntitySpecialityId(request.getDoctorClinicalEntitySpecialityId());
		planning.setAgendaVisualization(request.getAgendaVisualization());
		planning.setFixedSchedule(request.isFixedSchedule());
		planning.setAppointmentDuration(request.getAppointmentDuration());
		return planning;
	}
	
	public static Planning map(MedicalSchedule medicalSchedule, PlanningRequest request, List<DayRequest> planningDays) {
		val planning = map(medicalSchedule, request);
		map(planning, planningDays);
		return planning;
	}
	
	public static void map(Planning planning, PlanningRequest request) {
		if (planning != null && request != null) {
			planning.setFixedSchedule(request.isFixedSchedule());
			
			if (request.getAgendaVisualization() != null) {
				planning.setAgendaVisualization(request.getAgendaVisualization());
			}
			
			if (request.getAppointmentDuration() != null) {
				planning.setAppointmentDuration(request.getAppointmentDuration());
			}
		}
	}
	
	public static void map(final Planning planning, List<DayRequest> planningDays) {
		planningDays.forEach(request -> {
			val planningDay = PlanningDayMapper.map(request);
			planning.addPlanningDays(planningDay);
		});
	}
	
}