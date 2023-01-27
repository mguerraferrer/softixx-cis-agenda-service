package mx.softixx.cis.cloud.agenda.exposition.service;

import java.time.LocalDate;

import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayRequest;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse.NwdResponse;

public interface NonWorkingDayService {
	
	NonWorkingDayResponse findByMedicalSchedule(MedicalSchedule schedule);
	
	NwdResponse findOne(MedicalSchedule schedule, LocalDate nwd);
	
	NonWorkingDayResponse addOrUpdate(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest);
	
	void delete(MedicalSchedule schedule);
	
	void delete(MedicalSchedule medicalSchedule, NonWorkingDayRequest nonWorkingDayRequest);
	
	void delete(MedicalSchedule schedule, LocalDate nwd);

	void deleteById(Long id);
	
}