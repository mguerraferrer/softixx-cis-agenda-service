package mx.softixx.cis.cloud.agenda.persistence.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import mx.softixx.cis.common.core.datetime.WeekDay;
import mx.softixx.cis.common.jpa.model.BaseEntity;

/**
 * Persistent class for entity stored in table "medical_schedule_planning"
 *
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 *
 */
@Entity
@Table(name = "medical_schedule_planning_day", schema = "agenda")
@SequenceGenerator(name = "default_gen", sequenceName = "agenda.medical_schedule_planning_day_id_seq", allocationSize = 1)
public class PlanningDay extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medical_schedule_planning_id", referencedColumnName = "id")
	private Planning planning;

	@Column(name = "day")
	@Enumerated(EnumType.STRING)
	private WeekDay day;

	@Column(name = "start_time")
	private LocalTime startTime;

	@Column(name = "end_time")
	private LocalTime endTime;

	@Column(name = "total_patients")
	private Integer totalPatients;

	@Column(name = "total_extra_slot")
	private Integer totalExtraSlot;

	public PlanningDay() {
	}

	public PlanningDay(WeekDay day, LocalTime startTime, LocalTime endTime, Integer totalPatients,
			Integer totalExtraSlot) {
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalPatients = totalPatients;
		this.totalExtraSlot = totalExtraSlot;
	}
	
	public PlanningDay(Planning planning, WeekDay day, LocalTime startTime, LocalTime endTime, Integer totalPatients,
			Integer totalExtraSlot) {
		this.planning = planning;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalPatients = totalPatients;
		this.totalExtraSlot = totalExtraSlot;
	}

	/* Getters and Setters */
	public Planning getPlanning() {
		return planning;
	}

	public void setPlanning(Planning planning) {
		this.planning = planning;
	}

	public WeekDay getDay() {
		return day;
	}

	public void setDay(WeekDay day) {
		this.day = day;
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

	public Integer getTotalPatients() {
		return totalPatients;
	}

	public void setTotalPatients(Integer totalPatients) {
		this.totalPatients = totalPatients;
	}

	public Integer getTotalExtraSlot() {
		return totalExtraSlot;
	}

	public void setTotalExtraSlot(Integer totalExtraSlot) {
		this.totalExtraSlot = totalExtraSlot;
	}

	@Override
	public String toString() {
		return "PlanningDay [id=" + id + ", planning=" + planning.getId() + ", day=" + day + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", totalPatients=" + totalPatients + ", totalExtraSlot=" + totalExtraSlot
				+ "]";
	}

}