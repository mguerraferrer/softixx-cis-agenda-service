package mx.softixx.cis.cloud.agenda.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.softixx.cis.cloud.agenda.persistence.model.Appointment;
import mx.softixx.cis.cloud.agenda.persistence.model.AppointmentNotification;
import mx.softixx.cis.cloud.agenda.persistence.model.AppointmentReminder;
import mx.softixx.cis.common.agenda.payload.NotificationType;

/**
 * Repository : AppointmentReminder
 * 
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 * 
 */
public interface AppointmentReminderRepository extends JpaRepository<AppointmentReminder, Long> {

	/**
	 * Returns the list of AppointmentReminder associated with a {@link Appointment}
	 * 
	 * @param appointment {@link Appointment}
	 * @return {@code List<AppointmentReminder>} or empty list
	 */
	List<AppointmentReminder> findByAppointment(Appointment appointment);

	/**
	 * Returns the list of AppointmentReminder associated with a {@link Appointment}
	 * if {@link AppointmentNotification#getNotificationType()} is equal to
	 * notificationType param
	 * 
	 * @param appointment      {@link Appointment}
	 * @param notificationType NotificationType
	 * @return {@code List<AppointmentReminder>} or empty list
	 */
	List<AppointmentReminder> findByAppointmentAndNotificationType(Appointment appointment,
			NotificationType notificationType);

}