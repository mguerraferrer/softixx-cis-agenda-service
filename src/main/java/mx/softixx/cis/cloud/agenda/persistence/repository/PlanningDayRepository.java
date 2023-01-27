package mx.softixx.cis.cloud.agenda.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningDay;
import mx.softixx.cis.common.core.datetime.WeekDay;

/**
 * Repository : Planning
 * 
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 * 
 */
public interface PlanningDayRepository extends JpaRepository<PlanningDay, Long> {

	/**
	 * Returns the list of {@link PlanningDay} associated with a {@link Planning}
	 * 
	 * @param schedulePlanning {@link Planning}
	 * @return {@code List<PlanningDay>} or empty list
	 */
	List<PlanningDay> findByPlanning(Planning planning);

	/**
	 * Returns the {@link PlanningDay} associated with a {@link Planning} and a day
	 * 
	 * @param schedulePlanning {@link Planning}
	 * @param day              String
	 * @return Planning or null
	 */
	PlanningDay findByPlanningAndDay(Planning planning, WeekDay day);
	
	/**
	 * Returns the {@link PlanningDay} associated with a {@link Planning#getId()} and a day
	 * 
	 * @param schedulePlanning {@link Planning}
	 * @param day              String
	 * @return Planning or null
	 */
	PlanningDay findByPlanning_IdAndDay(Long planningId, WeekDay day);

}