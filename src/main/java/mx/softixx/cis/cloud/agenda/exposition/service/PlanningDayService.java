package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.List;

import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningDay;
import mx.softixx.cis.common.agenda.payload.DayRequest;
import mx.softixx.cis.common.agenda.payload.DayResponse;
import mx.softixx.cis.common.core.datetime.WeekDay;

public interface PlanningDayService {
	
	List<PlanningDay> findAll(Planning planning);
	
	List<DayResponse> findByPlanning(Planning planning);
	
	PlanningDay findById(Long id);
	
	PlanningDay findOne(Long id);
	
	PlanningDay findOne(Long id, WeekDay day);
	
	DayResponse create(Planning planning, DayRequest request);
	
	List<PlanningDay> create(Planning planning, List<DayRequest> planningDays);
	
	DayResponse update(Long id, DayRequest request);
	
	void delete(Planning planning);
	
	void delete(Long id);
	
}