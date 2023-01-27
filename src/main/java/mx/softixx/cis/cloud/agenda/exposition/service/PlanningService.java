package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.List;

import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.PlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.PlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.PlanningRequest;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;

public interface PlanningService {

	List<Planning> findAll(MedicalSchedule schedule);

	List<PlanningResponse> findByMedicalSchedule(MedicalSchedule schedule);

	Planning findById(Long id);

	Planning findOne(Long id);

	PlanningResponse create(MedicalSchedule medicalSchedule, MedicalSchedulePlanningFixedRequest planningFixedRequest);

	PlanningResponse create(MedicalSchedule medicalSchedule, MedicalSchedulePlanningDayRequest planningWeekDayRequest);

	PlanningResponse create(MedicalSchedule medicalSchedule, PlanningFixedRequest planningFixedRequest);

	PlanningResponse create(MedicalSchedule medicalSchedule, PlanningDayRequest planningDayRequest);

	PlanningResponse update(Long planningId, PlanningRequest planningRequest);

	PlanningResponse update(Long planningId, PlanningFixedRequest planningFixedRequest);

	PlanningResponse update(Long planningId, PlanningDayRequest planningDayRequest);

	void delete(MedicalSchedule schedule);

	void delete(Long id);

}