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
import mx.softixx.cis.cloud.agenda.exposition.service.AppointmentCancellationService;
import mx.softixx.cis.cloud.agenda.exposition.service.AppointmentCloneService;
import mx.softixx.cis.cloud.agenda.exposition.service.AppointmentConfirmationService;
import mx.softixx.cis.cloud.agenda.exposition.service.AppointmentRescheduleService;
import mx.softixx.cis.cloud.agenda.exposition.service.AppointmentService;
import mx.softixx.cis.common.agenda.payload.AppointmentCancellationRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentCloneRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentCloneResponse;
import mx.softixx.cis.common.agenda.payload.AppointmentConfirmationRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentRescheduleRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;
import mx.softixx.cis.common.validation.group.OnCreate;
import mx.softixx.cis.common.validation.group.OnUpdate;

@RestController
@RequestMapping("/api/v1/appointments")
@Validated
public class AppointmentController {

	private final AppointmentService appointmentService;
	private final AppointmentConfirmationService appointmentConfirmationService;
	private final AppointmentCancellationService appointmentCancellationService;
	private final AppointmentRescheduleService appointmentRescheduleService;
	private final AppointmentCloneService appointmentCloneService;

	public AppointmentController(AppointmentService appointmentService,
								 AppointmentConfirmationService appointmentConfirmationService, 
								 AppointmentCancellationService appointmentCancellationService, 
								 AppointmentRescheduleService appointmentRescheduleService, 
								 AppointmentCloneService appointmentCloneService) {
		this.appointmentService = appointmentService;
		this.appointmentConfirmationService = appointmentConfirmationService;
		this.appointmentCancellationService = appointmentCancellationService;
		this.appointmentRescheduleService = appointmentRescheduleService;
		this.appointmentCloneService = appointmentCloneService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppointmentResponse> findOne(@PathVariable Long id) {
		val response = appointmentService.findOne(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/folio/{folio}")
	public ResponseEntity<AppointmentResponse> findByFolio(@PathVariable String folio) {
		val response = appointmentService.findByFolio(folio);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	@Validated(OnCreate.class)
	public ResponseEntity<AppointmentResponse> create(@RequestBody @Valid AppointmentRequest request) {
		val response = appointmentService.create(request);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	@Validated(OnUpdate.class)
	public ResponseEntity<AppointmentResponse> update(@PathVariable Long id,
													  @RequestBody @Valid AppointmentRequest request) {
		val response = appointmentService.update(id, request);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/confirm")
	@Validated(OnUpdate.class)
	public ResponseEntity<AppointmentResponse> confirm(@PathVariable Long id,
													   @RequestBody @Valid AppointmentConfirmationRequest request) {
		val response = appointmentConfirmationService.confirm(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}/cancel")
	public ResponseEntity<AppointmentResponse> cancel(@PathVariable Long id,
													  @RequestBody @Valid AppointmentCancellationRequest request) {
		val response = appointmentCancellationService.cancel(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}/reschedule")
	public ResponseEntity<AppointmentResponse> reschedule(@PathVariable Long id,
													   	  @RequestBody @Valid AppointmentRescheduleRequest request) {
		val response = appointmentRescheduleService.reschedule(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}/clone")
	public ResponseEntity<AppointmentCloneResponse> clone(@PathVariable Long id,
													 @RequestBody @Valid AppointmentCloneRequest request) {
		val response = appointmentCloneService.clone(id, request);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	@Validated(OnUpdate.class)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		appointmentService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}