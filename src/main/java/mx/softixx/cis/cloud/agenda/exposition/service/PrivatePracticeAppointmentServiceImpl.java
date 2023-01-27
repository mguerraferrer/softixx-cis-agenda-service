package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.AppointmentMapper;
import mx.softixx.cis.cloud.agenda.persistence.repository.AppointmentRepository;
import mx.softixx.cis.common.agenda.exception.AppointmentNotFoundException;
import mx.softixx.cis.common.agenda.payload.AppointmentFindByBaseRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentFindByPersonRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;
import mx.softixx.cis.common.pageable.payload.SortRequest;
import mx.softixx.cis.common.pageable.util.SortUtils;

@Service
@Transactional(readOnly = true)
public class PrivatePracticeAppointmentServiceImpl implements PrivatePracticeAppointmentService {
	
	private final AppointmentRepository appointmentRepository;
	
	public PrivatePracticeAppointmentServiceImpl(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}
	
	private static final List<SortRequest> SORT_BY = List.of(new SortRequest("appointmentDate", "asc"),
			new SortRequest("startTime", "asc"));
	
	@Override
	public List<AppointmentResponse> findAll(AppointmentFindByBaseRequest request) {
		val dsId = request.getSpecialityId();
		val date = request.getAppointmentDate();
		val sort = SortUtils.sort(SORT_BY);
		
		val source = appointmentRepository.privatePracticeBook(dsId, date, sort);
		return source.stream().map(AppointmentMapper::map).toList();
	}
	
	@Override
	public AppointmentResponse findByPatient(AppointmentFindByPersonRequest request) {
		val dsId = request.getSpecialityId();
		val pid = request.getPersonId();
		val sid = request.getServiceId();
		val date = request.getAppointmentDate();
		
		return appointmentRepository.findByPatientAndDateInPrivatePractice(dsId, sid, pid, date)
									.map(AppointmentMapper::map)
									.orElseThrow(() -> new AppointmentNotFoundException(dsId, null, pid, date));
	}

}