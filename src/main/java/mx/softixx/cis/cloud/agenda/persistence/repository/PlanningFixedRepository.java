package mx.softixx.cis.cloud.agenda.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningFixed;

/**
 * Repository : PlanningFixed
 * 
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 * 
 */
public interface PlanningFixedRepository extends JpaRepository<PlanningFixed, Long> {

	/**
	 * Returns the {@link PlanningFixed} associated with a {@link Planning}
	 * 
	 * @param planning {@link Planning}
	 * @return {@code Optional<Planning>} or {@code Optional#empty()}
	 */
	Optional<PlanningFixed> findByPlanning(Planning planning);	

}