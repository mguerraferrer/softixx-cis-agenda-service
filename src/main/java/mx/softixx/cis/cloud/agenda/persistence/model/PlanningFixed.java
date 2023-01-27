package mx.softixx.cis.cloud.agenda.persistence.model;

import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Persistent class for entity stored in table "medical_schedule_planning"
 *
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 *
 */
@Entity
@Table(name = "medical_schedule_planning_fixed", schema = "agenda")
public class PlanningFixed {

	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@MapsId
	@JoinColumn(name = "id")
	private Planning planning;

	@Column(name = "start_time")
	private LocalTime startTime;

	@Column(name = "end_time")
	private LocalTime endTime;

	@Column(name = "total_patients")
	private Integer totalPatients;

	@Column(name = "total_extra_slot")
	private Integer totalExtraSlot;

	@Column(name = "days")
	private String days;

	public PlanningFixed() {
	}

	public PlanningFixed(Planning planning) {
		this.planning = planning;
	}

	/* Getters and Setters */
	public Planning getPlanning() {
		return planning;
	}

	public void setPlanning(Planning planning) {
		this.planning = planning;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	/* toString */
	@Override
	public String toString() {
		return "PlanningFixed [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", totalPatients="
				+ totalPatients + ", totalExtraSlot=" + totalExtraSlot + ", days=" + days + "]";
	}

}