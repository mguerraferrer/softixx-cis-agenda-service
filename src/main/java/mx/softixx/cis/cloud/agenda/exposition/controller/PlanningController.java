package mx.softixx.cis.cloud.agenda.exposition.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.observavility.MedicalScheduleObservation;
import mx.softixx.cis.cloud.agenda.exposition.observavility.PlanningObservation;
import mx.softixx.cis.common.agenda.payload.PlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.PlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.PlanningRequest;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;
import mx.softixx.cis.common.validation.group.OnCreate;
import mx.softixx.cis.common.validation.group.OnUpdate;

@RestController
@RequestMapping("/api/v1/plannings")
@Validated
public class PlanningController {

	private final MedicalScheduleObservation medicalScheduleObservation;
	private final PlanningObservation planningObservation;

	public PlanningController(MedicalScheduleObservation medicalScheduleObservation,
							  PlanningObservation planningObservation) {
		this.medicalScheduleObservation = medicalScheduleObservation;
		this.planningObservation = planningObservation;
	}

	@GetMapping("/medical-schedules/{msid}")
	public ResponseEntity<List<PlanningResponse>> findByMedicalSchedule(@PathVariable Long msid) {
		val medicalSchedule = medicalScheduleObservation.findById(msid);
		val response = planningObservation.findByMedicalSchedule(medicalSchedule);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/medical-schedules/{msid}/fixed")
	@Validated(OnCreate.class)
	public ResponseEntity<PlanningResponse> create(@PathVariable Long msid,
												   @RequestBody @Valid PlanningFixedRequest planningFixedRequest) {
		val medicalSchedule = medicalScheduleObservation.findById(msid);
		val response = planningObservation.create(medicalSchedule, planningFixedRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/medical-schedules/{msid}/days")
	@Validated(OnCreate.class)
	public ResponseEntity<PlanningResponse> create(@PathVariable Long msid, 
												   @RequestBody @Valid PlanningDayRequest planningDayRequest) {
		val medicalSchedule = medicalScheduleObservation.findById(msid);
		val response = planningObservation.create(medicalSchedule, planningDayRequest);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{pid}")
	@Validated(OnUpdate.class)
	public ResponseEntity<PlanningResponse> update(@PathVariable Long pid,
												   @RequestBody @Valid PlanningRequest planningRequest) {
		val response = planningObservation.update(pid, planningRequest);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{pid}/fixed")
	@Validated(OnUpdate.class)
	public ResponseEntity<PlanningResponse> update(@PathVariable Long pid,
												   @RequestBody @Valid PlanningFixedRequest planningFixedRequest) {
		return null;
	}

	@PutMapping("/{pid}/days")
	@Validated(OnUpdate.class)
	public ResponseEntity<PlanningResponse> update(@PathVariable Long pid,
												   @RequestBody @Valid PlanningDayRequest planningDayRequest) {
		val response = planningObservation.update(pid, planningDayRequest);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/medical-schedules/{msid}")
	public ResponseEntity<Void> deleteByMedicalSchedule(@PathVariable Long msid) {
		val medicalSchedule = medicalScheduleObservation.findById(msid);
		planningObservation.deleteByMedicalSchedule(medicalSchedule);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{pid}")
	public ResponseEntity<Void> deleteById(@PathVariable Long pid) {
		planningObservation.deleteById(pid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}