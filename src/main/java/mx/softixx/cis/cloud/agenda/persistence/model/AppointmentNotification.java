package mx.softixx.cis.cloud.agenda.persistence.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import mx.softixx.cis.common.agenda.payload.NotificationType;
import mx.softixx.cis.common.jpa.model.BaseEntity;

/**
 * Persistent class for entity stored in table "appointment_notification"
 *
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 *
 */

@Entity
@Table(name = "appointment_notification", schema = "agenda")
@SequenceGenerator(name = "default_gen", sequenceName = "agenda.appointment_notification_id_seq", allocationSize = 1)
public class AppointmentNotification extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", referencedColumnName = "id")
	private Appointment appointment;

	@Column(name = "notification_type")
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;

	@Column(name = "notified_emails", length = 500)
	private String notifiedEmails;

	@Column(name = "notified_mobiles", length = 100)
	private String notifiedMobiles;

	@Column(name = "notification_date", nullable = false)
	private LocalDateTime notificationDate;

	public AppointmentNotification() {
	}

	public AppointmentNotification(Appointment appointment, NotificationType notificationType, String notifiedEmails,
			String notifiedMobiles) {
		this.appointment = appointment;
		this.notificationType = notificationType;
		this.notifiedEmails = notifiedEmails;
		this.notifiedMobiles = notifiedMobiles;
		this.notificationDate = LocalDateTime.now();
	}

	/* Getters and Setters */
	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotifiedEmails() {
		return notifiedEmails;
	}

	public void setNotifiedEmails(String notifiedEmails) {
		this.notifiedEmails = notifiedEmails;
	}

	public String getNotifiedMobiles() {
		return notifiedMobiles;
	}

	public void setNotifiedMobiles(String notifiedMobiles) {
		this.notifiedMobiles = notifiedMobiles;
	}

	public LocalDateTime getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(LocalDateTime notificationDate) {
		this.notificationDate = notificationDate;
	}

	/* toString */
	@Override
	public String toString() {
		return "AppointmentNotification [id=" + id + ", appointment=" + appointment.getId() + ", notificationType="
				+ notificationType + ", notifiedEmails=" + notifiedEmails + ", notifiedMobiles=" + notifiedMobiles
				+ ", notificationDate=" + notificationDate + "]";
	}

}