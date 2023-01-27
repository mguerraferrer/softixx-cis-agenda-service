package mx.softixx.cis.cloud.agenda.exposition.mapper;

import java.util.Collections;
import java.util.List;

import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningDay;
import mx.softixx.cis.common.agenda.payload.DayRequest;
import mx.softixx.cis.common.agenda.payload.DayResponse;

public final class PlanningDayMapper {

	private PlanningDayMapper() {
	}

	public static DayResponse map(PlanningDay planningDay) {
		if (planningDay == null) {
			return null;
		}

		return DayResponse
				.builder()
				.id(planningDay.getId())
				.planningId(planningDay.getPlanning().getId())
				.day(planningDay.getDay())
				.startTime(planningDay.getStartTime())
				.endTime(planningDay.getEndTime())
				.totalPatients(planningDay.getTotalPatients())
				.totalExtraSlot(planningDay.getTotalExtraSlot())
				.build();
	}
	
	public static List<DayResponse> map(List<PlanningDay> planningDays) {
		if (planningDays != null) {
			return planningDays.stream().map(PlanningDayMapper::map).toList();
		}
		return Collections.emptyList();
	}
	
	public static PlanningDay map(DayRequest request) {
		if (request == null) {
			return null;
		}
		return new PlanningDay(request.getDay(), request.getStartTime(), request.getEndTime(),
				request.getTotalPatients(), request.getTotalExtraSlot());
	}
	
	public static PlanningDay map(Planning planning, DayRequest request) {
		if (request == null) {
			return null;
		}
		return new PlanningDay(planning, request.getDay(), request.getStartTime(), request.getEndTime(),
				request.getTotalPatients(), request.getTotalExtraSlot());
	}
	
	public static PlanningDay map(PlanningDay planningDay, DayRequest request) {
		if (request == null) {
			return null;
		}
		
		planningDay.setStartTime(request.getStartTime());
		planningDay.setEndTime(request.getEndTime());
		planningDay.setTotalPatients(request.getTotalPatients());
		planningDay.setTotalExtraSlot(request.getTotalExtraSlot());
		
		return planningDay;
	}

}