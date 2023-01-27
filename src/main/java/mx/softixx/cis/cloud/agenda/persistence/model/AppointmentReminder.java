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
 * Persistent class for entity stored in table "appointment_reminder"
 *
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 *
 */

@Entity
@Table(name = "appointment_reminder", schema = "agenda")
@SequenceGenerator(name = "default_gen", sequenceName = "agenda.appointment_reminder_id_seq", allocationSize = 1)
public class AppointmentReminder extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", referencedColumnName = "id")
	private Appointment appointment;

	@Column(name = "notification_type")
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;

	@Column(name = "mails_to_notify", nullable = false, length = 500)
	private String mailsToNotify;

	@Column(name = "mobiles_to_notified", nullable = false, length = 100)
	private String mobilesToNotify;

	@Column(name = "reminder_date", nullable = false)
	private LocalDateTime reminderDate;

	public AppointmentReminder() {
	}

	public AppointmentReminder(Appointment appointment, NotificationType notificationType, String mailsToNotify,
			String mobilesToNotify) {
		this.appointment = appointment;
		this.notificationType = notificationType;
		this.mailsToNotify = mailsToNotify;
		this.mobilesToNotify = mobilesToNotify;
		this.reminderDate = LocalDateTime.now();
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

	public String getMailsToNotify() {
		return mailsToNotify;
	}

	public void setMailsToNotify(String mailsToNotify) {
		this.mailsToNotify = mailsToNotify;
	}

	public String getMobilesToNotify() {
		return mobilesToNotify;
	}

	public void setMobilesToNotify(String mobilesToNotify) {
		this.mobilesToNotify = mobilesToNotify;
	}

	public LocalDateTime getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(LocalDateTime reminderDate) {
		this.reminderDate = reminderDate;
	}

	/* toString */
	@Override
	public String toString() {
		return "AppointmentReminder [id=" + id + ", appointment=" + appointment.getId() + ", notificationType="
				+ notificationType + ", mailsToNotify=" + mailsToNotify + ", mobilesToNotify=" + mobilesToNotify
				+ ", reminderDate=" + reminderDate + "]";
	}

}