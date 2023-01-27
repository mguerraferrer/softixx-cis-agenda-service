package mx.softixx.cis.cloud.agenda.exposition.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.service.PrivatePracticeAppointmentService;
import mx.softixx.cis.common.agenda.payload.AppointmentFindByBaseRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentFindByPersonRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

@RestController
@RequestMapping("/api/v1/appointments/private-practice")
@Validated
public class PrivatePracticeAppointmentController {
	
	private final PrivatePracticeAppointmentService privatePracticeAppointmentService;

	public PrivatePracticeAppointmentController(PrivatePracticeAppointmentService privatePracticeAppointmentService) {
		this.privatePracticeAppointmentService = privatePracticeAppointmentService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<AppointmentResponse>> findAll(@RequestParam Long dsid, 
															 @RequestParam Long sid,
															 @RequestParam LocalDate date) {
		val request = new AppointmentFindByBaseRequest(dsid, sid, date);
		val response = privatePracticeAppointmentService.findAll(request);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<AppointmentResponse> findByPatient(@RequestParam Long dsid, 
															 @RequestParam Long sid,
															 @RequestParam Long pid,
															 @RequestParam LocalDate date) {
		val request = new AppointmentFindByPersonRequest(dsid, sid, pid, date);
		val response = privatePracticeAppointmentService.findByPatient(request);
		return ResponseEntity.ok(response);
	}
	
}