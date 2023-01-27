package mx.softixx.cis.cloud.agenda.exposition.mapper;

import java.util.Comparator;

import lombok.val;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.NonWorkingDay;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse;
import mx.softixx.cis.common.agenda.payload.NonWorkingDayResponse.NwdResponse;

public final class NonWorkingDayMapper {

	private NonWorkingDayMapper() {		
	}
	
	public static NonWorkingDayResponse map(MedicalSchedule medicalSchedule) {
		if (medicalSchedule == null) {
			return null;
		}
		
		val nonWorkingDays = medicalSchedule.getNonWorkingDays().stream()
																.map(NonWorkingDayMapper::map)
																.sorted(Comparator.comparing(NwdResponse::getDate)).toList();
		return NonWorkingDayResponse
				.builder()
				.medicalScheduleId(medicalSchedule.getId())
				.nonWorkingDays(nonWorkingDays)
				.build();
	}
	
	public static NwdResponse map(NonWorkingDay nonWorkingDay) {
		if (nonWorkingDay == null) {
			return null;
		}
		
		return NwdResponse
				.builder()
				.id(nonWorkingDay.getId())
				.date(nonWorkingDay.getNwd())
				.year(nonWorkingDay.getYear())
				.month(nonWorkingDay.getMonth())
				.day(nonWorkingDay.getDay())
				.build();
	}
	
}