package mx.softixx.cis.cloud.agenda.persistence.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import mx.softixx.cis.common.jpa.model.BaseEntity;

/**
 * Persistent class for entity stored in table "medical_schedule_nwd"
 *
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 *
 */

@Entity
@Table(name = "medical_schedule_nwd", schema = "agenda")
@SequenceGenerator(name = "default_gen", sequenceName = "agenda.medical_schedule_nwd_id_seq", allocationSize = 1)
public class NonWorkingDay extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medical_schedule_id", referencedColumnName = "id")
	private MedicalSchedule medicalSchedule;

	@Column(name = "nwd", nullable = false)
	private LocalDate nwd;

	@Column(name = "year")
	private Integer year;

	@Column(name = "month")
	private Integer month;

	@Column(name = "day")
	private Integer day;

	public NonWorkingDay() {		
	}
	
	public NonWorkingDay(MedicalSchedule medicalSchedule, LocalDate nwd) {
		this.medicalSchedule = medicalSchedule;
		this.nwd = nwd;
		this.year = nwd.getYear();
		this.month = nwd.getMonth().getValue();
		this.day = nwd.getDayOfMonth();
	}

	public NonWorkingDay (LocalDate nwd) {
		this.nwd = nwd;
		this.year = nwd.getYear();
		this.month = nwd.getMonth().getValue();
		this.day = nwd.getDayOfMonth();
	}

	/* Getters and Setters */
	public MedicalSchedule getMedicalSchedule() {
		return medicalSchedule;
	}

	public void setMedicalSchedule(MedicalSchedule medicalSchedule) {
		this.medicalSchedule = medicalSchedule;
	}

	public LocalDate getNwd() {
		return nwd;
	}

	public void setNwd(LocalDate nwd) {
		this.nwd = nwd;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	/* toString */
	@Override
	public String toString() {
		return "NonWorkingDay [id=" + id + ", medicalSchedule=" + medicalSchedule.getId() + ", nwd=" + nwd + ", year="
				+ year + ", month=" + month + ", day=" + day + "]";
	}

}