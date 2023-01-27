package mx.softixx.cis.cloud.agenda.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.AppointmentMapper;
import mx.softixx.cis.cloud.agenda.persistence.model.Appointment;
import mx.softixx.cis.cloud.agenda.persistence.repository.AppointmentRepository;
import mx.softixx.cis.cloud.agenda.validator.AppointmentValidator;
import mx.softixx.cis.common.agenda.exception.AppointmentNotFoundException;
import mx.softixx.cis.common.agenda.payload.AppointmentRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

@Service
@Transactional(readOnly = true)
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentValidator appointmentValidator;
	private final AppointmentRepository appointmentRepository;
	
	public AppointmentServiceImpl(AppointmentValidator appointmentValidator,
								  AppointmentRepository appointmentRepository) {
		this.appointmentValidator = appointmentValidator;
		this.appointmentRepository = appointmentRepository;
	}

	@Override
	public Appointment findById(Long id) {
		return appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
	}

	@Override
	public AppointmentResponse findOne(Long id) {
		return appointmentRepository.findById(id).map(AppointmentMapper::map)
				.orElseThrow(() -> new AppointmentNotFoundException(id));
	}

	@Override
	public AppointmentResponse findByFolio(String folio) {
		return appointmentRepository.findByFolio(folio).map(AppointmentMapper::map)
				.orElseThrow(() -> new AppointmentNotFoundException(folio));
	}

	@Override
	@Transactional
	public AppointmentResponse create(AppointmentRequest request) {
		appointmentValidator.verifyRequestForCreate(request);
		val appointment = AppointmentMapper.map(request);
		return saveAndReturn(appointment);
	}

	@Override
	@Transactional
	public AppointmentResponse update(Long id, AppointmentRequest request) {
		val appointment = findById(id);
		appointmentValidator.verifyRequestForUpdate(request);
		AppointmentMapper.map(appointment, request);
		return saveAndReturn(appointment);
	}
	
	@Override
	@Transactional
	public AppointmentResponse saveAndReturn(Appointment appointment) {
		save(appointment);
		return AppointmentMapper.map(appointment);
	}
	
	@Override
	@Transactional
	public Appointment save(Appointment appointment) {
		if (appointment != null) {
			return appointmentRepository.save(appointment);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		val appointment = findById(id);
		appointmentRepository.delete(appointment);
	}

}