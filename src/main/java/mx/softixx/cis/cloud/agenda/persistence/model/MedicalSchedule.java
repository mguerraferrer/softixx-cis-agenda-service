package mx.softixx.cis.cloud.agenda.persistence.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import mx.softixx.cis.common.core.datetime.LocalDateUtils;
import mx.softixx.cis.common.jpa.model.BaseEntity;

/**
 * Persistent class for entity stored in table "medical_schedule"
 *
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 *
 */

@Entity
@Table(name = "medical_schedule", schema = "agenda")
@SequenceGenerator(name = "default_gen", sequenceName = "agenda.medical_schedule_id_seq", allocationSize = 1)
public class MedicalSchedule extends BaseEntity {

	@Column(name = "doctor_id")
	private Long doctorId;

	@Column(name = "private_practice_id")
	private Long privatePracticeId;

	@Column(name = "clinical_entity_id")
	private Long clinicalEntityId;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "active")
	private boolean active;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "medicalSchedule", targetEntity = Planning.class)
	private List<Planning> plannings;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "medicalSchedule", targetEntity = NonWorkingDay.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NonWorkingDay> nonWorkingDays;

	public MedicalSchedule withPrivatePractice(Long doctorId, Long privatePracticeId, LocalDate endDate) {
		this.doctorId = doctorId;
		this.privatePracticeId = privatePracticeId;
		this.endDate = endDate;
		this.active = LocalDateUtils.isFutureOrPresent(endDate);
		return this;
	}

	public MedicalSchedule withClinicalEntity(Long doctorId, Long clinicalEntityId, LocalDate endDate) {
		this.doctorId = doctorId;
		this.clinicalEntityId = clinicalEntityId;
		this.endDate = endDate;
		this.active = LocalDateUtils.isFutureOrPresent(endDate);
		return this;
	}

	/* Getters and Setters */
	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getPrivatePracticeId() {
		return privatePracticeId;
	}

	public void setPrivatePracticeId(Long privatePracticeId) {
		this.privatePracticeId = privatePracticeId;
	}

	public Long getClinicalEntityId() {
		return clinicalEntityId;
	}

	public void setClinicalEntityId(Long clinicalEntityId) {
		this.clinicalEntityId = clinicalEntityId;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Planning> getPlannings() {
		if (plannings == null) {
			plannings = new ArrayList<>();
		}
		return plannings;
	}

	public void setPlannings(List<Planning> plannings) {
		this.plannings = plannings;
	}
	
	public List<NonWorkingDay> getNonWorkingDays() {
		if (nonWorkingDays == null) {
			nonWorkingDays = new ArrayList<>();
		}
		return nonWorkingDays;
	}

	public void setNonWorkingDays(List<NonWorkingDay> nonWorkingDays) {
		this.nonWorkingDays = nonWorkingDays;
	}
	
	public void addNonWorkingDays(LocalDate nwd) {
		if (nonWorkingDays == null) {
			nonWorkingDays = new ArrayList<>();
		}
		this.nonWorkingDays.add(new NonWorkingDay(this, nwd));
	}
	
	public void addNonWorkingDays(List<NonWorkingDay> nonWorkingDays) {
		if (this.nonWorkingDays != null) {
			this.nonWorkingDays.clear();
			this.nonWorkingDays.addAll(nonWorkingDays);
		}
	}

	/* toString */
	@Override
	public String toString() {
		return "MedicalSchedule [id=" + id + ", doctorId=" + doctorId + ", privatePracticeId=" + privatePracticeId
				+ ", clinicalEntityId=" + clinicalEntityId + ", endDate=" + endDate + ", active=" + active + "]";
	}

}