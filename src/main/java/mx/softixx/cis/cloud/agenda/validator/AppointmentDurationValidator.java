package mx.softixx.cis.cloud.agenda.validator;

import lombok.val;
import mx.softixx.cis.common.agenda.exception.PlanningAppointmentDurationException;

/**
 * @author Maikel Guerra Ferrer - mguerraferrer@gmail.com
 *
 */
public final class AppointmentDurationValidator {
	
	private AppointmentDurationValidator() {		
	}
	
	private static final Integer DIVISOR = 5;
	private static final Integer MIN = 30;
	
	public static void validate(Integer duration) {
		if (duration != null) {
			val isValid = duration >= MIN && duration % DIVISOR == 0;
			if (!isValid) {
				throw new PlanningAppointmentDurationException();
			}
		}
	}

}