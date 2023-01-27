package mx.softixx.cis.cloud.agenda.persistence.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.val;
import mx.softixx.cis.common.agenda.payload.AppointmentCancelled;
import mx.softixx.cis.common.agenda.payload.AppointmentConfirmation;
import mx.softixx.cis.common.agenda.payload.AppointmentOrigin;
import mx.softixx.cis.common.agenda.payload.AppointmentReschedule;
import mx.softixx.cis.common.agenda.payload.AppointmentStatus;
import mx.softixx.cis.common.agenda.payload.AppointmentType;
import mx.softixx.cis.common.core.crypto.CryptoUtils;
import mx.softixx.cis.common.jpa.model.BaseEntity;

/**
 * Persistent class for entity stored in table "appointment"
 *
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 *
 */

@Entity
@Table(name = "appointment", schema = "agenda")
@SequenceGenerator(name = "default_gen", sequenceName = "agenda.appointment_id_seq", allocationSize = 1)
public class Appointment extends BaseEntity {

	@Column(name = "person_id")
	private Long personId;

	@Column(name = "doctor_speciality_id")
	private Long doctorSpecialityId;

	@Column(name = "private_practice_service_id")
	private Long privatePracticeServiceId;

	@Column(name = "doctor_clinical_entity_speciality_id")
	private Long doctorClinicalEntitySpecialityId;

	@Column(name = "doctor_clinical_entity_service_id")
	private Long doctorClinicalEntityServiceId;
	
	@Column(name = "appointment_type")
	@Enumerated(EnumType.STRING)
	private AppointmentType type;
	
	@Column(name = "origin")
	@Enumerated(EnumType.STRING)
	private AppointmentOrigin origin;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private AppointmentStatus status;
	
	@Column(name = "confirmation")
	@Enumerated(EnumType.STRING)
	private AppointmentConfirmation confirmation;

	@Column(name = "cancelled_by")
	@Enumerated(EnumType.STRING)
	private AppointmentCancelled cancelledBy;

	@Column(name = "rescheduled_by")
	@Enumerated(EnumType.STRING)
	private AppointmentReschedule rescheduledBy;

	@Column(name = "another_person_name", length = 100)
	private String anotherPersonName;

	@Column(name = "relationship_id")
	private Long relationshipId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", referencedColumnName = "id")
	private Appointment originalAppointment;

	@Column(name = "appointment_date", nullable = false)
	private LocalDate appointmentDate;
	
	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;
	
	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;

	@Column(name = "folio", nullable = false, length = 50)
	private String folio;

	@Column(name = "month")
	private Integer month;

	@Column(name = "additional_info", length = 2147483647)
	private String additionalInfo;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "appointment", targetEntity = AppointmentReminder.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AppointmentReminder> appointmentReminders;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "appointment", targetEntity = AppointmentNotification.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AppointmentNotification> appointmentNotifications;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "originalAppointment", targetEntity = Appointment.class)
	private List<Appointment> appointments;

	public Appointment() {
		this.folio = CryptoUtils.generateHash().toUpperCase();
	}

	public Appointment(Long doctorSpecialityId, Long privatePracticeServiceId, Long doctorClinicalEntitySpecialityId,
			Long doctorClinicalEntityServiceId, Long personId, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
		this.doctorSpecialityId = doctorSpecialityId;
		this.privatePracticeServiceId = privatePracticeServiceId;
		this.doctorClinicalEntitySpecialityId = doctorClinicalEntitySpecialityId;
		this.doctorClinicalEntityServiceId = doctorClinicalEntityServiceId;
		this.personId = personId;
		this.appointmentDate = appointmentDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.folio = CryptoUtils.generateHash().toUpperCase();
		this.month = appointmentDate.getMonthValue();
	}

	public static Appointment clone(Appointment appointment, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
		if (appointment != null) {
			val clone = new Appointment(appointment.getDoctorSpecialityId(), appointment.getPrivatePracticeServiceId(),
					appointment.getDoctorClinicalEntitySpecialityId(), appointment.getDoctorClinicalEntityServiceId(),
					appointment.getPersonId(), appointmentDate, startTime, endTime);
			clone.setOriginalAppointment(appointment);
			return clone;
		}
		return null;
	}
	
	public Appointment reschedule(Appointment appointment, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
		val reschedule = clone(appointment, appointmentDate, startTime, endTime);
		if (reschedule != null) {
			reschedule.setOriginalAppointment(appointment);
			reschedule.setType(appointment.getType());
			reschedule.setOrigin(appointment.getOrigin());
			reschedule.setStatus(AppointmentStatus.CREATED);
			reschedule.setConfirmation(AppointmentConfirmation.UNCONFIRMED);
			return reschedule;
		}
		return null;
	}

	/* Getters and Setters */
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getDoctorSpecialityId() {
		return doctorSpecialityId;
	}

	public void setDoctorSpecialityId(Long doctorSpecialityId) {
		this.doctorSpecialityId = doctorSpecialityId;
	}

	public Long getPrivatePracticeServiceId() {
		return privatePracticeServiceId;
	}

	public void setPrivatePracticeServiceId(Long privatePracticeServiceId) {
		this.privatePracticeServiceId = privatePracticeServiceId;
	}

	public Long getDoctorClinicalEntitySpecialityId() {
		return doctorClinicalEntitySpecialityId;
	}

	public void setDoctorClinicalEntitySpecialityId(Long doctorClinicalEntitySpecialityId) {
		this.doctorClinicalEntitySpecialityId = doctorClinicalEntitySpecialityId;
	}

	public Long getDoctorClinicalEntityServiceId() {
		return doctorClinicalEntityServiceId;
	}

	public void setDoctorClinicalEntityServiceId(Long doctorClinicalEntityServiceId) {
		this.doctorClinicalEntityServiceId = doctorClinicalEntityServiceId;
	}
	
	public AppointmentType getType() {
		return type;
	}

	public void setType(AppointmentType type) {
		this.type = type;
	}

	public AppointmentOrigin getOrigin() {
		return origin;
	}

	public void setOrigin(AppointmentOrigin origin) {
		this.origin = origin;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public AppointmentConfirmation getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(AppointmentConfirmation confirmation) {
		this.confirmation = confirmation;
	}

	public AppointmentCancelled getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(AppointmentCancelled cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public AppointmentReschedule getRescheduledBy() {
		return rescheduledBy;
	}

	public void setRescheduledBy(AppointmentReschedule rescheduledBy) {
		this.rescheduledBy = rescheduledBy;
	}

	public String getAnotherPersonName() {
		return anotherPersonName;
	}

	public void setAnotherPersonName(String anotherPersonName) {
		this.anotherPersonName = anotherPersonName;
	}

	public Long getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(Long relationshipId) {
		this.relationshipId = relationshipId;
	}

	public Appointment getOriginalAppointment() {
		return originalAppointment;
	}

	public void setOriginalAppointment(Appointment originalAppointment) {
		this.originalAppointment = originalAppointment;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public List<AppointmentReminder> getAppointmentReminders() {
		if (appointmentReminders == null) {
			appointmentReminders = new ArrayList<>();
		}
		return appointmentReminders;
	}

	public void setAppointmentReminders(List<AppointmentReminder> appointmentReminders) {
		this.appointmentReminders = appointmentReminders;
	}

	public void addReminders(AppointmentReminder appointmentReminder) {
		if (appointmentReminders == null) {
			appointmentReminders = new ArrayList<>();
		}
		this.appointmentReminders.add(new AppointmentReminder(this, appointmentReminder.getNotificationType(),
				appointmentReminder.getMailsToNotify(), appointmentReminder.getMobilesToNotify()));
	}

	public void addReminders(List<AppointmentReminder> appointmentReminders) {
		if (this.appointmentReminders != null) {
			this.appointmentReminders.clear();
			this.appointmentReminders.addAll(appointmentReminders);
		}
	}

	public List<AppointmentNotification> getAppointmentNotifications() {
		if (appointmentNotifications == null) {
			appointmentNotifications = new ArrayList<>();
		}
		return appointmentNotifications;
	}

	public void setAppointmentNotifications(List<AppointmentNotification> appointmentNotifications) {
		this.appointmentNotifications = appointmentNotifications;
	}

	public void addNotifications(AppointmentNotification appointmentNotification) {
		if (appointmentNotifications == null) {
			appointmentNotifications = new ArrayList<>();
		}
		this.appointmentNotifications.add(new AppointmentNotification(this, appointmentNotification.getNotificationType(),
				appointmentNotification.getNotifiedEmails(), appointmentNotification.getNotifiedMobiles()));
	}

	public void addNotifications(List<AppointmentNotification> appointmentNotifications) {
		if (this.appointmentNotifications != null) {
			this.appointmentNotifications.clear();
			this.appointmentNotifications.addAll(appointmentNotifications);
		}
	}

	public List<Appointment> getAppointments() {
		if (appointments == null) {
			appointments = new ArrayList<>();
		}
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	
	@Transient
	public Long getOriginalAppointmentId() {
		if (this.originalAppointment != null) {
			return originalAppointment.getId();
		}
		return null;
	}

	/* toString */
	@Override
	public String toString() {
		var originalAppointmentId = originalAppointment != null ? originalAppointment.getId() : null;
		return "Appointment [personId=" + personId + ", doctorSpecialityId=" + doctorSpecialityId
				+ ", privatePracticeServiceId=" + privatePracticeServiceId + ", doctorClinicalEntitySpecialityId="
				+ doctorClinicalEntitySpecialityId + ", doctorClinicalEntityServiceId=" + doctorClinicalEntityServiceId
				+ ", type=" + type + ", origin=" + origin + ", status=" + status + ", confirmation=" + confirmation
				+ ", cancelledBy=" + cancelledBy + ", rescheduledBy=" + rescheduledBy + ", anotherPersonName="
				+ anotherPersonName + ", relationshipId=" + relationshipId + ", originalAppointmentId=" + originalAppointmentId
				+ ", appointmentDate=" + appointmentDate + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", folio=" + folio + ", month=" + month + ", additionalInfo=" + additionalInfo + "]";
	}
	
}