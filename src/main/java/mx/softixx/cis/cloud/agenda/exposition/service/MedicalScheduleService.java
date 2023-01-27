package mx.softixx.cis.cloud.agenda.exposition.service;

import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleRequest;
import mx.softixx.cis.common.agenda.payload.MedicalScheduleResponse;

public interface MedicalScheduleService {
	
	MedicalSchedule findById(Long id);
	
	MedicalSchedule findByDoctorAndPrivatePractice(Long doctorId, Long privatePracticeId);
	
	MedicalScheduleResponse findOneByDoctorAndPrivatePractice(Long doctorId, Long privatePracticeId);
	
	MedicalSchedule findByDoctorAndClinicalEntity(Long doctorId, Long clinicalEntityId);
	
	MedicalScheduleResponse findOneByDoctorAndClinicalEntity(Long doctorId, Long clinicalEntityId);
	
	MedicalScheduleResponse create(MedicalScheduleRequest request);
	
	MedicalScheduleResponse create(MedicalSchedulePlanningRequest request);
	
	MedicalSchedule update(MedicalSchedule medicalSchedule);
	
	MedicalScheduleResponse updateByPrivatePractice(Long doctorId, Long privatePracticeId, MedicalScheduleRequest request);
	
	MedicalScheduleResponse updateByClinicalEntity(Long doctorId, Long clinicalEntityId, MedicalScheduleRequest request);
	
	void deleteByPrivatePractice(Long doctorId, Long privatePracticeId);
	
	void deleteByClinicalEntity(Long doctorId, Long clinicalEntityId);
	
}