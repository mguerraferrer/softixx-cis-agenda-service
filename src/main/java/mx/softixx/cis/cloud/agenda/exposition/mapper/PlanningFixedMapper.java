package mx.softixx.cis.cloud.agenda.exposition.mapper;

import lombok.val;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningFixed;
import mx.softixx.cis.common.agenda.payload.FixedRequest;
import mx.softixx.cis.common.agenda.payload.FixedResponse;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.core.collection.ListUtils;
import mx.softixx.cis.common.core.datetime.WeekDay;

public final class PlanningFixedMapper {

	private PlanningFixedMapper() {		
	}
	
	public static FixedRequest map(MedicalSchedulePlanningFixedRequest request) {
		if (request == null) {
			return null;
		}
		return request.getPlanningFixed();
	}
	
	public static PlanningFixed map(Planning planning, FixedRequest request) {
		if (planning == null || request == null) {
			return null;
		}
		
		val planningFixed = new PlanningFixed(planning);
		planningFixed.setStartTime(request.getStartTime());
		planningFixed.setEndTime(request.getEndTime());
		populate(planningFixed, request);
		
		return planningFixed;
	}
	
	public static void map(final PlanningFixed planningFixed, FixedRequest request) {
		if (planningFixed != null && request != null) {
			if (request.getStartTime() != null) {
				planningFixed.setStartTime(request.getStartTime());
			}
			
			if (request.getEndTime() != null) {
				planningFixed.setEndTime(request.getEndTime());
			}
			
			populate(planningFixed, request);
		}
	}

	public static FixedResponse map(PlanningFixed planningFixed) {
		if (planningFixed == null) {
			return null;
		}
		
		val weekDays = ListUtils.toList(planningFixed.getDays()).stream().map(WeekDay::valueOf).toList();
		return FixedResponse
				.builder()
				.id(planningFixed.getId())
				.planningId(planningFixed.getPlanning().getId())
				.startTime(planningFixed.getStartTime())
				.endTime(planningFixed.getEndTime())
				.totalPatients(planningFixed.getTotalPatients())
				.totalExtraSlot(planningFixed.getTotalExtraSlot())
				.weekDays(weekDays)
				.build();
	}
	
	private static void populate(PlanningFixed planningFixed, FixedRequest request) {
		planningFixed.setTotalPatients(request.getTotalPatients());
		planningFixed.setTotalExtraSlot(request.getTotalExtraSlot());
		planningFixed.setDays(ListUtils.toString(request.getDays()));
	}
	
}