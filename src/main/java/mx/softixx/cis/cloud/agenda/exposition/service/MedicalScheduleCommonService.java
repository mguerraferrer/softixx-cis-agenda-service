package mx.softixx.cis.cloud.agenda.exposition.service;

import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleResponse;

public interface MedicalScheduleCommonService {
	
	MedicalSchedule findById(Long id);
	
	MedicalSchedule findOne(Long id);
	
	MedicalSchedule findByDoctor(Long doctorId);
	
	MedicalScheduleResponse findOneByDoctor(Long doctorId);
	
}