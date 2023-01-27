package mx.softixx.cis.cloud.agenda.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;

/**
 * Repository : MedicalSchedule
 * 
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 * 
 */
public interface MedicalScheduleRepository extends JpaRepository<MedicalSchedule, Long> {

	/**
	 * Returns the MedicalSchedule associated with a doctor id
	 * 
	 * @param doctorId Long
	 * @return {@code Optional<MedicalSchedule>} or {@code Optional#empty()}
	 */
	Optional<MedicalSchedule> findByDoctorId(Long doctorId);

	/**
	 * Returns the MedicalSchedule associated with a doctor id and a private
	 * practice id
	 * 
	 * @param doctorId          Long
	 * @param privatePracticeId Long
	 * @return {@code Optional<MedicalSchedule>} or {@code Optional#empty()}
	 */
	Optional<MedicalSchedule> findByDoctorIdAndPrivatePracticeId(Long doctorId, Long privatePracticeId);

	/**
	 * Returns the MedicalSchedule associated with a doctor id and a
	 * {@link clinical}
	 * 
	 * @param doctorId         Long
	 * @param clinicalEntityId Long
	 * @return {@code Optional<MedicalSchedule>} or {@code Optional#empty()}
	 */
	Optional<MedicalSchedule> findByDoctorIdAndClinicalEntityId(Long doctorId, Long clinicalEntityId);

}