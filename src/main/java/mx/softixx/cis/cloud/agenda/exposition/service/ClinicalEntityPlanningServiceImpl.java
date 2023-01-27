package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.PlanningMapper;
import mx.softixx.cis.cloud.agenda.persistence.model.MedicalSchedule;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningFixed;
import mx.softixx.cis.cloud.agenda.persistence.repository.PlanningRepository;
import mx.softixx.cis.common.agenda.exception.PlanningDoctorClinicalEntitySpecialityAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.PlanningDoctorClinicalEntitySpecialityRequiredException;
import mx.softixx.cis.common.agenda.exception.PlanningNotFoundException;
import mx.softixx.cis.common.agenda.payload.DayRequest;
import mx.softixx.cis.common.agenda.payload.FixedRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningDayRequest;
import mx.softixx.cis.common.agenda.payload.MedicalSchedulePlanningFixedRequest;
import mx.softixx.cis.common.agenda.payload.PlanningRequest;
import mx.softixx.cis.common.agenda.payload.PlanningResponse;
import mx.softixx.cis.common.core.collection.ListUtils;
import mx.softixx.cis.common.core.datetime.WeekDay;

@Service
public class ClinicalEntityPlanningServiceImpl implements ClinicalEntityPlanningService {
	
	private final PlanningFixedService planningFixedService;
	private final PlanningDayService planningDayService;
	private final PlanningRepository planningRepository;
	
	public ClinicalEntityPlanningServiceImpl(PlanningFixedService planningFixedService,
											  PlanningDayService planningDayService, 
											  PlanningRepository planningRepository) {
		this.planningFixedService = planningFixedService;
		this.planningDayService = planningDayService;
		this.planningRepository = planningRepository;
	}

	@Override
	public PlanningResponse findOne(MedicalSchedule schedule, Long dcesId) {
		val planning = find(schedule, dcesId);
		return planningDetails(planning);
	}

	@Override
	public PlanningResponse findOne(MedicalSchedule schedule, Long dcesId, WeekDay day) {
		val planning = find(schedule, dcesId);
		if (planning != null) {
			if (planning.isFixedSchedule()) {
				val planningFixed = planningFixedService.findByPlanning(planning);
				val planningFixedByWeekDay = planningFixedByWeekDay(planningFixed, day);
				return PlanningMapper.map(planning, planningFixedByWeekDay);
			} else {
				val planningDays = planning.getPlanningDays().stream().filter(i -> i.getDay().equals(day)).toList();
				if (!planningDays.isEmpty()) {
					planning.setPlanningDays(planningDays);
					return PlanningMapper.map(planning);
				}
			}
		}
		return null;
	}
	
	@Override
	public PlanningResponse planningDetails(Planning planning) {
		if (planning != null) {
			if (planning.isFixedSchedule()) {
				val planningFixed = planningFixedService.findByPlanning(planning);
				return PlanningMapper.map(planning, planningFixed);
			} else {
				return PlanningMapper.map(planning);
			}
		}
		return null;
	}
	
	@Override
	@Transactional
	public PlanningResponse create(MedicalSchedule medicalSchedule, MedicalSchedulePlanningFixedRequest planningFixedRequest) {
		return create(medicalSchedule, planningFixedRequest.getPlanning(), planningFixedRequest.getPlanningFixed());
	}
	
	@Override
	@Transactional
	public PlanningResponse create(MedicalSchedule medicalSchedule, MedicalSchedulePlanningDayRequest planningWeekDayRequest) {
		return create(medicalSchedule, planningWeekDayRequest.getPlanning(), planningWeekDayRequest.getPlanningDays());
	}

	@Override
	@Transactional
	public PlanningResponse create(MedicalSchedule medicalSchedule, PlanningRequest planningRequest, FixedRequest fixedRequest) {
		verifyRequest(medicalSchedule, planningRequest);
		
		val planning = PlanningMapper.map(medicalSchedule, planningRequest);
		save(planning);
		
		val planningFixed = planningFixedService.create(planning, fixedRequest);
		return PlanningMapper.map(planning, planningFixed);
	}

	@Override
	@Transactional
	public PlanningResponse create(MedicalSchedule medicalSchedule, PlanningRequest planningRequest, List<DayRequest> planningDays) {
		verifyRequest(medicalSchedule, planningRequest);
		
		val planning = PlanningMapper.map(medicalSchedule, planningRequest);
		save(planning);
		
		if (planning != null) {
			val planningDayResponseList = planningDayService.create(planning, planningDays);
			planning.setPlanningDays(planningDayResponseList);
			return PlanningMapper.map(planning);
		}
		return null;
	}

	@Override
	@Transactional
	public PlanningResponse update(Long id, PlanningRequest planningRequest) {
		val planning = planningRepository.findById(id).orElseThrow(() -> new PlanningNotFoundException(id));
		PlanningMapper.map(planning, planningRequest);
		return planningDetails(planning);
	}

	private Planning find(MedicalSchedule schedule, Long dcesId) {
		return planningRepository.findByMedicalScheduleAndDoctorClinicalEntitySpecialityId(schedule, dcesId).orElse(null);
	}
	
	private PlanningFixed planningFixedByWeekDay(PlanningFixed planningFixed, WeekDay day) {
		if (planningFixed != null) {
			val exists = ListUtils.toList(planningFixed.getDays()).stream().anyMatch(i -> day.name().equals(i));
			return exists ? planningFixed : null;
		}
		return null;
	}
	
	private void verifyRequest(MedicalSchedule medicalSchedule, PlanningRequest request) {
		if (request.getDoctorClinicalEntitySpecialityId() == null) {
			throw new PlanningDoctorClinicalEntitySpecialityRequiredException();
		}
		
		val planning = find(medicalSchedule, request.getDoctorClinicalEntitySpecialityId());
		if (planning != null) {
			throw new PlanningDoctorClinicalEntitySpecialityAlreadyExistsException();
		}
	}
	
	private Planning save(Planning planning) {
		if (planning != null) {
			return planningRepository.save(planning);
		}
		return null;
	}

}