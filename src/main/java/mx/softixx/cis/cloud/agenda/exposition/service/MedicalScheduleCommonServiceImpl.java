package mx.softixx.cis.cloud.agenda.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.MedicalScheduleMapper;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.repository.MedicalScheduleRepository;
import mx.softixx.cis.common.agenda.exception.MedicalScheduleNotFoundException;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleResponse;

@Service
@Transactional(readOnly = true)
public class MedicalScheduleCommonServiceImpl implements MedicalScheduleCommonService {

	private final MedicalScheduleRepository medicalScheduleRepository;

	public MedicalScheduleCommonServiceImpl(MedicalScheduleRepository medicalScheduleRepository) {
		this.medicalScheduleRepository = medicalScheduleRepository;
	}

	@Override
	public MedicalSchedule findById(Long id) {
		return medicalScheduleRepository.findById(id).orElseThrow(() -> new MedicalScheduleNotFoundException(id));
	}

	@Override
	public MedicalSchedule findOne(Long id) {
		return medicalScheduleRepository.findById(id).orElse(null);
	}

	@Override
	public MedicalSchedule findByDoctor(Long doctorId) {
		return medicalScheduleRepository.findByDoctorId(doctorId)
				.orElseThrow(() -> new MedicalScheduleNotFoundException(doctorId, null, null));
	}

	@Override
	public MedicalScheduleResponse findOneByDoctor(Long doctorId) {
		val medicalSchedule = medicalScheduleRepository.findByDoctorId(doctorId).orElse(null);
		return MedicalScheduleMapper.map(medicalSchedule);
	}

}