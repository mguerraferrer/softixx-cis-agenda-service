package mx.softixx.cis.cloud.agenda.persistence.repository.querybuilder;

public final class AppointmentQueryBuilder {

	private AppointmentQueryBuilder() {
	}

	private static final String FROM = "FROM Appointment a ";
	private static final String PERSON = "AND a.personId = :pid ";
	private static final String DATE = "AND a.appointmentDate = :date ";
	private static final String START_TIME = "AND a.startTime = :st ";
	private static final String RESCHEDULEDBY_CANCELLEDBY_NULLABLE = "AND rescheduledBy IS NULL AND cancelledBy IS NULL ";
	private static final String STATUS_CREATED = "AND status = CREATED";
	private static final String STATUS_COMPLETED = "AND status <> COMPLETED";
	private static final String DOCTOR_SPECIALITY_WHERE = "WHERE a.doctorSpecialityId = :dsid ";
	private static final String DOCTOR_CE_SPECIALITY_WHERE = "WHERE a.doctorClinicalEntitySpecialityId = :dcesid ";
	private static final String DOCTOR_SPECIALITY_SERVICE_WHERE = DOCTOR_SPECIALITY_WHERE + " AND a.privatePracticeServiceId = :sid ";
	private static final String DOCTOR_CE_SPECIALITY_SERVICE_WHERE = DOCTOR_CE_SPECIALITY_WHERE + " AND doctorClinicalEntityServiceId = :sid ";

	public static final String FIND_BY_FOLIO = FROM + "WHERE a.folio = :folio " + RESCHEDULEDBY_CANCELLEDBY_NULLABLE;

	public static final String DOCTOR_SPECIALITY_APPOINTMENTS_QUERY = FROM + DOCTOR_SPECIALITY_WHERE + DATE
			+ RESCHEDULEDBY_CANCELLEDBY_NULLABLE + STATUS_COMPLETED;

	public static final String FIND_BY_DOCTOR_SPECIALITY_AND_PERSON = FROM + DOCTOR_SPECIALITY_SERVICE_WHERE + PERSON + DATE
			+ STATUS_CREATED;
	
	public static final String FIND_BY_DOCTOR_CE_SPECIALITY_AND_PERSON = FROM + DOCTOR_CE_SPECIALITY_SERVICE_WHERE + PERSON + DATE
			+ STATUS_CREATED;
	
	public static final String FIND_BY_DS_AND_DATE_TIME = FROM + DOCTOR_SPECIALITY_SERVICE_WHERE + DATE + START_TIME
			+ STATUS_CREATED;
	
	public static final String FIND_BY_CES_AND_DATE_TIME = FROM + DOCTOR_CE_SPECIALITY_SERVICE_WHERE + DATE + START_TIME
			+ STATUS_CREATED;

}