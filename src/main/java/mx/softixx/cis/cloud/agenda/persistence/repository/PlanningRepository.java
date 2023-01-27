package mx.softixx.cis.cloud.agenda.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;

/**
 * Repository : Planning
 * 
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 * 
 */
public interface PlanningRepository extends JpaRepository<Planning, Long> {

	/**
	 * Returns the list of Planning associated with a {@link MedicalSchedule} if
	 * {@link Planning#isFixedSchedule()} is true
	 * 
	 * @param schedule {@link MedicalSchedule}
	 * @return {@code List<Planning>} or empty list
	 */
	List<Planning> findByMedicalSchedule(MedicalSchedule schedule);

	// ----------------------------
	// ----- DoctorSpeciality -----
	// ----------------------------
	/**
	 * Returns the {@link Planning} by id if {@link Planning#isFixedSchedule()} is
	 * true
	 * 
	 * @param schedule           {@link MedicalSchedule}
	 * @param doctorSpecialityId Long
	 * @return {@code Optional<Planning>} or {@code Optional#empty()}
	 */
	// Optional<Planning> findById(Long id);

	/**
	 * Returns the {@link Planning} by id if {@link Planning#isFixedSchedule()} is
	 * false
	 * 
	 * @param schedule           {@link MedicalSchedule}
	 * @param doctorSpecialityId Long
	 * @return {@code Optional<Planning>} or {@code Optional#empty()}
	 */
	// Optional<Planning> findById(Long id);

	/**
	 * Returns the {@link Planning} associated with a {@link MedicalSchedule} and a
	 * doctor speciality id
	 * 
	 * @param schedule           {@link MedicalSchedule}
	 * @param doctorSpecialityId Long
	 * @return {@code Optional<Planning>} or {@code Optional#empty()}
	 */
	Optional<Planning> findByMedicalScheduleAndDoctorSpecialityId(MedicalSchedule schedule, Long doctorSpecialityId);

	// ------------------------------------------
	// ----- DoctorClinicalEntitySpeciality -----
	// ------------------------------------------
	/**
	 * Returns the {@link Planning} associated with a {@link MedicalSchedule} and a
	 * doctor clinical entity speciality id if {@link Planning#isFixedSchedule()} is
	 * true
	 * 
	 * @param schedule                   {@link MedicalSchedule}
	 * @param doctorClinicalSpecialityId Long
	 * @return {@code Optional<Planning>} or {@code Optional#empty()}
	 */
	Optional<Planning> findByMedicalScheduleAndDoctorClinicalEntitySpecialityId(MedicalSchedule schedule,
			Long doctorClinicalSpecialityId);

}