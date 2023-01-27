package mx.softixx.cis.cloud.agenda.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.PlanningFixedMapper;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningFixed;
import mx.softixx.cis.cloud.agenda.persistence.repository.PlanningFixedRepository;
import mx.softixx.cis.common.agenda.exception.PlanningNotFoundException;
import mx.softixx.cis.common.agenda.payload.FixedRequest;
import mx.softixx.cis.common.core.collection.ListUtils;
import mx.softixx.cis.common.core.datetime.WeekDay;

@Service
@Transactional(readOnly = true)
public class PlanningFixedServiceImpl implements PlanningFixedService {

	private final PlanningFixedRepository planningFixedRepository;

	public PlanningFixedServiceImpl(PlanningFixedRepository planningFixedRepository) {
		this.planningFixedRepository = planningFixedRepository;
	}

	@Override
	public PlanningFixed findByPlanning(Planning planning) {
		return planningFixedRepository.findByPlanning(planning)
				.orElseThrow(() -> new PlanningNotFoundException(planning.getId()));
	}

	@Override
	public PlanningFixed findOneByPlanning(Planning planning) {
		return planningFixedRepository.findByPlanning(planning).orElse(null);
	}

	@Override
	public PlanningFixed findById(Long id) {
		return planningFixedRepository.findById(id).orElseThrow(() -> new PlanningNotFoundException(id));
	}

	@Override
	public PlanningFixed findOne(Long id) {
		return planningFixedRepository.findById(id).orElse(null);
	}

	@Override
	public PlanningFixed findByWeekDay(Planning planning, WeekDay day) {
		val planningFixed = findByPlanning(planning);
		return planningFixedByWeekDay(planningFixed, day);
	}

	@Override
	public PlanningFixed findOneByWeekDay(Planning planning, WeekDay day) {
		val planningFixed = findOneByPlanning(planning);
		return planningFixedByWeekDay(planningFixed, day);
	}

	@Override
	@Transactional
	public PlanningFixed create(Planning planning, FixedRequest request) {
		val planningFixed = PlanningFixedMapper.map(planning, request);
		return save(planningFixed);
	}

	@Override
	@Transactional
	public PlanningFixed update(Planning planning, FixedRequest request) {
		val planningFixed = findByPlanning(planning);
		PlanningFixedMapper.map(planningFixed, request);
		return save(planningFixed);
	}
	
	@Override
	@Transactional
	public PlanningFixed update(Long id, FixedRequest request) {
		val planningFixed = findById(id);
		PlanningFixedMapper.map(planningFixed, request);
		return save(planningFixed);
	}

	@Override
	@Transactional
	public void delete(Planning planning) {
		val planningFixed = findByPlanning(planning);
		deletePlanningFixed(planningFixed);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		val planningFixed = findById(id);
		deletePlanningFixed(planningFixed);
	}

	private PlanningFixed planningFixedByWeekDay(PlanningFixed planningFixed, WeekDay day) {
		if (planningFixed != null) {
			val exists = ListUtils.toList(planningFixed.getDays()).stream().anyMatch(i -> day.name().equals(i));
			return exists ? planningFixed : null;
		}
		return null;
	}
	
	private PlanningFixed save(PlanningFixed planningFixed) {
		if (planningFixed != null) {
			return planningFixedRepository.save(planningFixed);
		}
		return null;
	}
	
	private void deletePlanningFixed(PlanningFixed planningFixed) {
		if (planningFixed != null) {
			planningFixedRepository.delete(planningFixed);
		}
	}

}