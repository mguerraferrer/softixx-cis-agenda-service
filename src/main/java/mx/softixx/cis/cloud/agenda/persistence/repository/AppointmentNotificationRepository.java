package mx.softixx.cis.cloud.agenda.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.softixx.cis.cloud.agenda.persistence.model.Appointment;
import mx.softixx.cis.cloud.agenda.persistence.model.AppointmentNotification;
import mx.softixx.cis.common.agenda.payload.NotificationType;

/**
 * Repository : AppointmentNotification
 * 
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 * 
 */
public interface AppointmentNotificationRepository extends JpaRepository<AppointmentNotification, Long> {

	/**
	 * Returns the list of AppointmentNotification associated with a
	 * {@link Appointment}
	 * 
	 * @param appointment {@link Appointment}
	 * @return {@code List<AppointmentNotification>} or empty list
	 */
	List<AppointmentNotification> findByAppointment(Appointment appointment);

	/**
	 * Returns the list of AppointmentNotification associated with a
	 * {@link Appointment} if {@link AppointmentNotification#getNotificationType()}
	 * is equal to notificationType param
	 * 
	 * @param appointment      {@link Appointment}
	 * @param notificationType NotificationType
	 * @return {@code List<AppointmentNotification>} or empty list
	 */
	List<AppointmentNotification> findByAppointmentAndNotificationType(Appointment appointment,
			NotificationType notificationType);

}