package mx.softixx.cis.cloud.agenda.persistence.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.softixx.cis.cloud.agenda.persistence.model.Appointment;
import mx.softixx.cis.cloud.agenda.persistence.repository.querybuilder.AppointmentQueryBuilder;

/**
 * Repository : Appointment
 * 
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 * 
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	@Query(AppointmentQueryBuilder.FIND_BY_FOLIO)
	Optional<Appointment> findByFolio(@Param("folio") String folio);

	/**
	 * Returns the list of Appointment associated with a doctor speciality id, a
	 * person id and a date
	 * 
	 * @param dsid     Long
	 * @param personId Long
	 * @param date     {@link LocalDate}
	 * @return {@code Optional<Appointment>} or {@code Optional#empty()} list
	 */
	@Query(AppointmentQueryBuilder.FIND_BY_DOCTOR_SPECIALITY_AND_PERSON)
	Optional<Appointment> findByPatientAndDateInPrivatePractice(@Param("dsid") Long dsid, @Param("sid") Long sid,
			@Param("pid") Long personId, @Param("date") LocalDate date);

	/**
	 * Returns the list of Appointment associated with a doctor speciality id, a
	 * person id and a date
	 * 
	 * @param dcesid   Long
	 * @param personId Long
	 * @param date     {@link LocalDate}
	 * @return {@code Optional<Appointment>} or {@code Optional#empty()} list
	 */
	@Query(AppointmentQueryBuilder.FIND_BY_DOCTOR_CE_SPECIALITY_AND_PERSON)
	Optional<Appointment> findByPatientAndDateInClinicalEntity(@Param("dcesid") Long dcesid, @Param("sid") Long sid,
			@Param("pid") Long personId, @Param("date") LocalDate date);

	/**
	 * Returns the list of Appointment associated with a doctor speciality id and a
	 * date if {@link Appointment#rescheduled()} is false,
	 * {@link Appointment#cancelledBy ()} is null and {@link Appointment#status ()}
	 * is equal to CREATED
	 * 
	 * @param dsid Long
	 * @param date {@link LocalDate}
	 * @return {@code List<Appointment>} or empty list
	 */
	@Query(AppointmentQueryBuilder.DOCTOR_SPECIALITY_APPOINTMENTS_QUERY)
	List<Appointment> privatePracticeBook(@Param("dsid") Long dsid, @Param("date") LocalDate date, Sort sort);

	@Query(AppointmentQueryBuilder.FIND_BY_DS_AND_DATE_TIME)
	Optional<Appointment> findByDateAndTimeInPrivatePractice(@Param("dsid") Long dsid, @Param("sid") Long sid,
			@Param("date") LocalDate date, @Param("st") LocalTime st);

	@Query(AppointmentQueryBuilder.FIND_BY_CES_AND_DATE_TIME)
	Optional<Appointment> findByDateAndTimeInClinicalEntity(@Param("dcesid") Long dcesid, @Param("sid") Long sid,
			@Param("date") LocalDate date, @Param("st") LocalTime st);

}