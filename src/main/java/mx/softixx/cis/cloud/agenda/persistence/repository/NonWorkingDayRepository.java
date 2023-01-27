package mx.softixx.cis.cloud.agenda.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.NonWorkingDay;

/**
 * Repository : NonWorkingDay
 * 
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 * 
 */
public interface NonWorkingDayRepository extends JpaRepository<NonWorkingDay, Long> {

	/**
	 * Returns the list of NonWorkingDay associated with a private practice id if
	 * {@link NonWorkingDay#isActive()} is true
	 * 
	 * @param privatePracticeId Long
	 * @return {@code List<NonWorkingDay>} or empty list
	 */
	List<NonWorkingDay> findByMedicalSchedule(MedicalSchedule medicalSchedule);

	/**
	 * Returns the NonWorkingDay associated with a private practice id and a date
	 * 
	 * @param privatePracticeId Long
	 * @param date              LocalDate
	 * @return NonWorkingDay or null
	 */
	Optional<NonWorkingDay> findByMedicalScheduleAndNwd(MedicalSchedule medicalSchedule, LocalDate date);

	/**
	 * Returns the list of NonWorkingDay associated with a private practice id and a
	 * month if {@link NonWorkingDay#isActive()} is true
	 * 
	 * @param privatePracticeId Long
	 * @param month             Integer
	 * @return {@code List<NonWorkingDay>} or empty list
	 */
	List<NonWorkingDay> findByMedicalScheduleAndMonth(MedicalSchedule medicalSchedule, Integer month);

	/**
	 * Returns the NonWorkingDay associated with a private practice id and a date if
	 * {@link NonWorkingDay#isActive()} is true sorted by sort param
	 * 
	 * @param privatePracticeId Long
	 * @param date              LocalDate
	 * @param sort              {@link Sort}
	 * @return {@code List<NonWorkingDay>} or empty list
	 */
	List<NonWorkingDay> findByMedicalScheduleAndNwdGreaterThanEqual(MedicalSchedule medicalSchedule, LocalDate date,
			Sort sort);

	/**
	 * Returns a {@code Page<NonWorkingDay>} object associated with a private
	 * practice id and a date if {@link NonWorkingDay#isActive()} is true paged by
	 * pageable param
	 * 
	 * @param privatePracticeId Long
	 * @param date              LocalDate
	 * @param pageable          {@link Pageable}
	 * @return {@code Page<NonWorkingDay>} or empty page
	 */
	Page<NonWorkingDay> findByMedicalScheduleAndNwdGreaterThanEqual(MedicalSchedule medicalSchedule, LocalDate date,
			Pageable pageable);

}