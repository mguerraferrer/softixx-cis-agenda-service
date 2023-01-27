package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.List;

import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.common.agenda.payload.DayRequest;
import mx.softixx.cis.common.agenda.payload.FixedRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.PlanningRequest;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;
import mx.softixx.cis.common.core.datetime.WeekDay;

public interface PrivatePracticePlanningService {

	PlanningResponse findOne(MedicalSchedule schedule, Long doctorSpecialityId);

	PlanningResponse findOne(MedicalSchedule schedule, Long doctorSpecialityId, WeekDay day);

	PlanningResponse planningDetails(Planning planning);

	PlanningResponse create(MedicalSchedule medicalSchedule, MedicalSchedulePlanningFixedRequest planningFixedRequest);

	PlanningResponse create(MedicalSchedule medicalSchedule, MedicalSchedulePlanningDayRequest planningWeekDayRequest);

	PlanningResponse create(MedicalSchedule medicalSchedule, PlanningRequest planningRequest,
			FixedRequest fixedRequest);

	PlanningResponse create(MedicalSchedule medicalSchedule, PlanningRequest planningRequest,
			List<DayRequest> planningDays);

	PlanningResponse update(Long id, PlanningRequest planningRequest);

}