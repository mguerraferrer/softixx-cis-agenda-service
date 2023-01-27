package mx.softixx.cis.cloud.agenda.exposition.controller;

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
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleResponse;
import mx.softixx.cis.common.validation.group.OnCreate;
import mx.softixx.cis.common.validation.group.OnUpdate;

@RestController
@RequestMapping("/api/v1/medical-schedules")
@Validated
public class MedicalScheduleController {

	private final MedicalScheduleObservation medicalScheduleObservation;

	public MedicalScheduleController(MedicalScheduleObservation medicalScheduleObservation) {
		this.medicalScheduleObservation = medicalScheduleObservation;
	}

	@GetMapping("/doctor/{did}/pp/{ppid}")
	public ResponseEntity<MedicalScheduleResponse> findByPrivPractice(@PathVariable Long did, @PathVariable Long ppid) {
		val response = medicalScheduleObservation.findByPrivPractice(did, ppid);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/doctor/{did}/ce/{ceid}")
	public ResponseEntity<MedicalScheduleResponse> findByClinicalEntity(@PathVariable Long did, @PathVariable Long ceid) {
		val response = medicalScheduleObservation.findByClinicalEntity(did, ceid);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	@Validated(OnCreate.class)
	public ResponseEntity<MedicalScheduleResponse> create(@RequestBody @Valid MedicalScheduleRequest request) {
		return ResponseEntity.ok(medicalScheduleObservation.create(request));
	}

	@PostMapping("/planning-fixed")
	@Validated(OnCreate.class)
	public ResponseEntity<MedicalScheduleResponse> createFixed(
			@RequestBody @Valid MedicalSchedulePlanningFixedRequest request) {
		return ResponseEntity.ok(medicalScheduleObservation.createFixed(request));
	}

	@PostMapping("/planning-day")
	@Validated(OnCreate.class)
	public ResponseEntity<MedicalScheduleResponse> createWeekDay(
			@RequestBody @Valid MedicalSchedulePlanningDayRequest request) {
		return ResponseEntity.ok(medicalScheduleObservation.createWeekDay(request));
	}

	@PutMapping("/doctor/{did}/pp/{ppid}")
	@Validated(OnUpdate.class)
	public ResponseEntity<MedicalScheduleResponse> updateByPrivatePractice(@PathVariable Long did,
																		   @PathVariable Long ppid, 
																		   @RequestBody @Valid MedicalScheduleRequest request) {
		return ResponseEntity.ok(medicalScheduleObservation.updateByPrivatePractice(did, ppid, request));
	}

	@PutMapping("/doctor/{did}/ce/{ceid}")
	@Validated(OnUpdate.class)
	public ResponseEntity<MedicalScheduleResponse> updateByClinicalEntity(@PathVariable Long did,
																		  @PathVariable Long ceid, 
																		  @RequestBody @Valid MedicalScheduleRequest request) {
		return ResponseEntity.ok(medicalScheduleObservation.updateByClinicalEntity(did, ceid, request));
	}

	@DeleteMapping("/doctor/{did}/pp/{ppid}")
	public ResponseEntity<Void> deleteByPrivatePractice(@PathVariable Long did, @PathVariable Long ppid) {
		medicalScheduleObservation.deleteByPrivatePractice(did, ppid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/doctor/{did}/ce/{ceid}")
	public ResponseEntity<Void> deleteByClinicalEntity(@PathVariable Long did, @PathVariable Long ceid) {
		medicalScheduleObservation.deleteByClinicalEntity(did, ceid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}