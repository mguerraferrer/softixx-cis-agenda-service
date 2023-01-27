package mx.softixx.cis.cloud.agenda.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.val;
import mx.softixx.cis.cloud.agenda.persistence.model.Appointment;
import mx.softixx.cis.cloud.agenda.persistence.repository.AppointmentRepository;
import mx.softixx.cis.common.agenda.exception.AppointmentCloneFrequencyException;
import mx.softixx.cis.common.agenda.payload.AppointmentCloneFrequency;
import mx.softixx.cis.common.agenda.payload.AppointmentCloneFrequencyInfo;
import mx.softixx.cis.common.agenda.payload.AppointmentCloneRequest;
import mx.softixx.cis.common.core.collection.ListUtils;
import mx.softixx.cis.common.core.datetime.WeekDay;
import mx.softixx.cis.common.core.message.MessageUtils;

@Component
public class AppointmentCloneValidator {

	private final AppointmentRepository appointmentRepository;

	public AppointmentCloneValidator(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}

	public void verifyRequest(Appointment appointment, AppointmentCloneRequest request) {
		val frequency = request.getFrequency();
		val frequencyInfo = request.getFrequencyInfo();
		val frequencyDay = request.getFrequencyDay();
		val frequencyEvery = request.getFrequencyEvery();
		val frequencyWeekDays = request.getFrequencyWeekDays();

		verifyFrequencyRequest(frequency, frequencyInfo, frequencyDay, frequencyEvery, frequencyWeekDays, appointment);
	}

	private void verifyFrequencyRequest(AppointmentCloneFrequency frequency,
			AppointmentCloneFrequencyInfo frequencyInfo, Integer frequencyDay, Integer frequencyEvery,
			List<WeekDay> frequencyWeekDays, Appointment appointment) {
		verifyCloneDaily(frequency, frequencyInfo, frequencyDay, frequencyEvery, frequencyWeekDays);
		verifyCloneWeekly(frequency, frequencyInfo, frequencyDay, frequencyEvery, frequencyWeekDays, appointment);
	}

	private void verifyCloneDaily(AppointmentCloneFrequency frequency, AppointmentCloneFrequencyInfo frequencyInfo,
			Integer frequencyDay, Integer frequencyEvery, List<WeekDay> frequencyWeekDays) {
		String errorMessage = null;
		var hasError = false;
		if (frequency.equals(AppointmentCloneFrequency.DAILY)) {
			errorMessage = MessageUtils.getMessage("exception.text.clone.daily.every.day");
			verifyEveryday(frequencyInfo, frequencyDay, frequencyEvery, frequencyWeekDays, errorMessage);

			if (frequencyInfo.equals(AppointmentCloneFrequencyInfo.EVERY_X_DAYS)) {
				if (frequencyDay != null || frequencyWeekDays != null) {
					errorMessage = MessageUtils.getMessage("exception.text.clone.daily.every.xdays");
					hasError = true;
				} else {
					if (frequencyEvery == null) {
						errorMessage = MessageUtils.getMessage("exception.text.clone.daily.every.xdays.frequency");
						hasError = true;
					} else if (frequencyEvery <= 1 || frequencyEvery > 6) {
						errorMessage = MessageUtils
								.getMessage("exception.text.clone.daily.every.xdays.frequency.invalid");
						hasError = true;
					}
				}
			}
		}

		if (hasError) {
			throw new AppointmentCloneFrequencyException(errorMessage);
		}
	}

	private void verifyCloneWeekly(AppointmentCloneFrequency frequency, AppointmentCloneFrequencyInfo frequencyInfo,
			Integer frequencyDay, Integer frequencyEvery, List<WeekDay> frequencyWeekDays, Appointment appointment) {
		String errorMessage = null;
		var hasError = false;
		if (frequency.equals(AppointmentCloneFrequency.WEEKLY)) {
			errorMessage = MessageUtils.getMessage("exception.text.clone.weekly.same.day");
			verifyEveryday(frequencyInfo, frequencyDay, frequencyEvery, frequencyWeekDays, errorMessage);
			verifyWeeklyEveryXWeeksSameDay(frequencyInfo, frequencyDay, frequencyEvery, frequencyWeekDays);

			if (frequencyInfo.equals(AppointmentCloneFrequencyInfo.EVERY_X_WEEKS_DAYS)) {
				verifyWeeklyEveryXWeeksDifferentsDays(frequencyDay, frequencyEvery, frequencyWeekDays, appointment);
				// Validar que los días de la semana del listado frequencyWeekDays sean válidos
				// en el calendario del médico
			}
		}

		if (hasError) {
			throw new AppointmentCloneFrequencyException(errorMessage);
		}
	}

	private void verifyEveryday(AppointmentCloneFrequencyInfo frequencyInfo, Integer frequencyDay,
			Integer frequencyEvery, List<WeekDay> frequencyWeekDays, String errorMessage) {
		if ((frequencyInfo.equals(AppointmentCloneFrequencyInfo.EVERYDAY)
				|| frequencyInfo.equals(AppointmentCloneFrequencyInfo.SAMEDAY))
				&& (frequencyDay != null || frequencyEvery != null || frequencyWeekDays != null)) {
			throw new AppointmentCloneFrequencyException(errorMessage);
		}
	}

	private void verifyWeeklyEveryXWeeksSameDay(AppointmentCloneFrequencyInfo frequencyInfo, Integer frequencyDay,
			Integer frequencyEvery, List<WeekDay> frequencyWeekDays) {
		if (frequencyInfo.equals(AppointmentCloneFrequencyInfo.EVERY_X_WEEKS_SAME_DAY)) {
			if (frequencyDay != null || frequencyWeekDays != null) {
				throw new AppointmentCloneFrequencyException(
						MessageUtils.getMessage("exception.text.clone.weekly.every.xweeks.sameday"));
			} else {
				if (frequencyEvery == null) {
					throw new AppointmentCloneFrequencyException(
							MessageUtils.getMessage("exception.text.clone.weekly.every.xweeks.sameday.required"));
				} else if (frequencyEvery < 2 || frequencyEvery > 3) {
					throw new AppointmentCloneFrequencyException(
							MessageUtils.getMessage("exception.text.clone.weekly.every.xweeks.sameday.invalid"));
				}
			}
		}
	}

	private void verifyWeeklyEveryXWeeksDifferentsDays(Integer frequencyDay, Integer frequencyEvery,
			List<WeekDay> frequencyWeekDays, Appointment appointment) {
		if (frequencyDay != null) {
			throw new AppointmentCloneFrequencyException(
					MessageUtils.getMessage("exception.text.clone.weekly.every.xweeks.days.invalid"));
		} else if (frequencyEvery == null || frequencyWeekDays == null || frequencyWeekDays.isEmpty()) {
			throw new AppointmentCloneFrequencyException(
					MessageUtils.getMessage("exception.text.clone.weekly.every.xweeks.days.frequency"));
		} else {
			if (frequencyEvery < 1 || frequencyEvery > 3) {
				throw new AppointmentCloneFrequencyException(
						MessageUtils.getMessage("exception.text.clone.weekly.every.xweeks.days.frequency.invalid"));
			}
			verifyFrequencyWeekDays(frequencyWeekDays, appointment);
		}
	}
	
	private void verifyFrequencyWeekDays(List<WeekDay> frequencyWeekDays, Appointment appointment) {
		val dayOfWeek = appointment.getAppointmentDate().getDayOfWeek();
		if (frequencyWeekDays.size() == 1) {
			val weekDay = frequencyWeekDays.get(0);
			if (dayOfWeek.name().equals(weekDay.name())) {
				throw new AppointmentCloneFrequencyException(
						MessageUtils.getMessage("exception.text.clone.weekly.every.xweeks.days.different"));
			}
		} else {
			val hasDuplicateDays = ListUtils.hasDuplicateValues(frequencyWeekDays);
			if (hasDuplicateDays) {
				throw new AppointmentCloneFrequencyException(
						MessageUtils.getMessage("exception.text.clone.weekly.every.xweeks.days.different.repeat"));
			}
		}
		
		
	}

}