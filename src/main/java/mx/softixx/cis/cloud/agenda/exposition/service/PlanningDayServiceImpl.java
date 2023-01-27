package mx.softixx.cis.cloud.agenda.exposition.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import mx.softixx.cis.cloud.agenda.exposition.mapper.PlanningDayMapper;
import mx.softixx.cis.cloud.agenda.persistence.model.Planning;
import mx.softixx.cis.cloud.agenda.persistence.model.PlanningDay;
import mx.softixx.cis.cloud.agenda.persistence.repository.PlanningDayRepository;
import mx.softixx.cis.common.agenda.exception.PlanningDayAlreadyExistsException;
import mx.softixx.cis.common.agenda.exception.PlanningDayNotFoundException;
import mx.softixx.cis.common.agenda.payload.DayRequest;
import mx.softixx.cis.common.agenda.payload.DayResponse;
import mx.softixx.cis.common.core.datetime.WeekDay;

@Service
@Transactional(readOnly = true)
public class PlanningDayServiceImpl implements PlanningDayService {
	
	private final PlanningDayRepository planningDayRepository;
	
	public PlanningDayServiceImpl(PlanningDayRepository planningDayRepository) {
		this.planningDayRepository = planningDayRepository;
	}

	@Override
	public List<PlanningDay> findAll(Planning planning) {
		return planningDayRepository.findByPlanning(planning);
	}

	@Override
	public List<DayResponse> findByPlanning(Planning planning) {
		return findAll(planning).stream().map(PlanningDayMapper::map).toList();
	}

	@Override
	public PlanningDay findById(Long id) {
		return planningDayRepository.findById(id).orElseThrow(() -> new PlanningDayNotFoundException(id));
	}

	@Override
	public PlanningDay findOne(Long id) {
		return planningDayRepository.findById(id).orElse(null);
	}
	
	@Override
	public PlanningDay findOne(Long id, WeekDay day) {
		return planningDayRepository.findByPlanning_IdAndDay(id, day);
	}

	@Override
	public DayResponse create(Planning planning, DayRequest request) {
		validateRequest(planning, request);
		val planningDay = PlanningDayMapper.map(planning, request);
		return saveAndReturn(planningDay);
	}
	
	@Override
	public List<PlanningDay> create(Planning planning, List<DayRequest> planningDays) {
		val targetList = new ArrayList<PlanningDay>();
		planningDays.forEach(request -> {
			validateRequest(planning, request);
			val planningDay = PlanningDayMapper.map(planning, request);
			val response = save(planningDay);
			targetList.add(response);
		});
		return targetList;
	}

	@Override
	public DayResponse update(Long id, DayRequest request) {
		var planningDay = findById(id);
		planningDay = PlanningDayMapper.map(planningDay, request);
		return saveAndReturn(planningDay);
	}

	@Override
	public void delete(Planning planning) {
		val source = findAll(planning);
		planningDayRepository.deleteAll(source);
	}
	
	@Override
	public void delete(Long id) {
		val planningDay = findById(id);
		planningDayRepository.delete(planningDay);
	}
	
	private void validateRequest(Planning planning, DayRequest request) {
		val planningDay = findOne(planning.getId(), request.getDay());
		if (planningDay != null) {
			throw new PlanningDayAlreadyExistsException();
		}
	}
	
	private DayResponse saveAndReturn(PlanningDay planningDay) {
		save(planningDay);
		return PlanningDayMapper.map(planningDay);
	}
	
	private PlanningDay save(PlanningDay planningDay) {
		if (planningDay != null) {
			return planningDayRepository.save(planningDay);
		}
		return null;
	}

}