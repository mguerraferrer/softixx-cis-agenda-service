package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.softixx.cis.common.agenda.payload.AppointmentFindByBaseRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentFindByPersonRequest;
import mx.softixx.cis.common.agenda.payload.AppointmentResponse;

@Service
@Transactional(readOnly = true)
public class ClinicalEntityAppointmentServiceImpl implements ClinicalEntityAppointmentService {

	@Override
	public AppointmentResponse findByPatient(AppointmentFindByPersonRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AppointmentResponse> findAll(AppointmentFindByBaseRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}