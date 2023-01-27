package mx.softixx.cis.cloud.agenda.exposition.controller;

import java.time.LocalDate;

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
import mx.softixx.cis.cloud.agenda.exposition.observavility.NonWorkingDayObservation;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayRequest;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse.NwdResponse;
import mx.softixx.cis.common.validation.group.OnCreate;
import mx.softixx.cis.common.validation.group.OnDelete;
import mx.softixx.cis.common.validation.group.OnUpdate;

@RestController
@RequestMapping("/api/v1/non-working-days")
@Validated
public class NonWorkingDayController {
	
	private final MedicalScheduleObservation medicalScheduleObservation;
	private final NonWorkingDayObservation nonWorkingDayObservation;

	public NonWorkingDayController(MedicalScheduleObservation medicalScheduleObservation,
			NonWorkingDayObservation nonWorkingDayObservation) {
		this.medicalScheduleObservation = medicalScheduleObservation;
		this.nonWorkingDayObservation = nonWorkingDayObservation;
	}
	
	@GetMapping("/medical-schedules/{msid}")
	public ResponseEntity<NonWorkingDayResponse> findByMedicalSchedule(@PathVariable Long msid) {
		val medicalSchedule = findMedicalSchedule(msid);
		val response = nonWorkingDayObservation.findByMedicalSchedule(medicalSchedule);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/medical-schedules/{msid}/nwd/{nwd}")
	public ResponseEntity<NwdResponse> findOne(@PathVariable Long msid, @PathVariable LocalDate nwd) {
		val medicalSchedule = findMedicalSchedule(msid);
		val response = nonWorkingDayObservation.findOne(medicalSchedule, nwd);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	@Validated(OnCreate.class)
	public ResponseEntity<NonWorkingDayResponse> add(@RequestBody @Valid NonWorkingDayRequest nonWorkingDayRequest) {
		val medicalSchedule = medicalScheduleObservation.findById(nonWorkingDayRequest.getMedicalScheduleId());
		val response = nonWorkingDayObservation.add(medicalSchedule, nonWorkingDayRequest);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/medical-schedules/{msid}")
	@Validated(OnUpdate.class)
	public ResponseEntity<NonWorkingDayResponse> update(@PathVariable Long msid, @RequestBody @Valid NonWorkingDayRequest nonWorkingDayRequest) {
		val medicalSchedule = findMedicalSchedule(msid);
		val response = nonWorkingDayObservation.update(medicalSchedule, nonWorkingDayRequest);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/medical-schedules/{msid}")
	public ResponseEntity<Void> delete(@PathVariable Long msid) {
		val medicalSchedule = findMedicalSchedule(msid);
		nonWorkingDayObservation.delete(medicalSchedule);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/medical-schedules/{msid}/bulk")
	@Validated(OnDelete.class)
	public ResponseEntity<Void> bulkDelete(@PathVariable Long msid, @RequestBody @Valid NonWorkingDayRequest nonWorkingDayRequest) {
		val medicalSchedule = findMedicalSchedule(msid);
		nonWorkingDayObservation.bulkDelete(medicalSchedule, nonWorkingDayRequest);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/medical-schedules/{msid}/nwd/{nwd}")
	public ResponseEntity<Void> deleteByNwd(@PathVariable Long msid, @PathVariable LocalDate nwd) {
		val medicalSchedule = findMedicalSchedule(msid);
		nonWorkingDayObservation.deleteByNwd(medicalSchedule, nwd);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/{pid}")
	public ResponseEntity<Void> deleteById(@PathVariable Long pid) {
		nonWorkingDayObservation.deleteById(pid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	private MedicalSchedule findMedicalSchedule(Long msid) {
		return medicalScheduleObservation.findById(msid);
	}
	
}