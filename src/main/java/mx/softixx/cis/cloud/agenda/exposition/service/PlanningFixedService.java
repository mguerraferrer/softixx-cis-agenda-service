package mx.softixx.cis.cloud.agenda.exposition.service;

import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningFixed;
import mx.softixx.cis.common.agenda.payload.FixedRequest;
import mx.softixx.cis.common.core.datetime.WeekDay;

public interface PlanningFixedService {
	
	PlanningFixed findByPlanning(Planning planning);
	
	PlanningFixed findOneByPlanning(Planning planning);
	
	PlanningFixed findById(Long id);
	
	PlanningFixed findOne(Long id);
	
	PlanningFixed findByWeekDay(Planning planning, WeekDay day);
	
	PlanningFixed findOneByWeekDay(Planning planning, WeekDay day);
	
	PlanningFixed create(Planning planning, FixedRequest request);
	
	PlanningFixed update(Planning planning, FixedRequest request);
	
	PlanningFixed update(Long id, FixedRequest request);
	
	void delete(Planning planning);
	
	void delete(Long id);
	
}