package com.greyhound.repository.custom.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.greyhound.dto.PaginationDataDto;
import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.TrainerByDateAndTrackRequestDto;
import com.greyhound.dto.TrainerCompareRequestDto;
import com.greyhound.dto.TrainerFilterDto;
import com.greyhound.dto.TrainerFilterDtoWithPagination;
import com.greyhound.dto.TrainerResponseDto;
import com.greyhound.model.Trainer;
import com.greyhound.repository.custom.TrainerRepositoryCustom;
import com.greyhound.service.ITrainerService;
import com.greyhound.utility.Utility;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public class TrainerRepositoryCustomImpl implements TrainerRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private ITrainerService trainerService;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@SuppressWarnings("unchecked")
	@Override
	public PaginationDto getTrainerByFilterWithPagination(TrainerFilterDtoWithPagination filterWithPagination) {
		List<TrainerResponseDto> dataList = new ArrayList<>();
		String query = "select gd.name as greyhound_name,count(r.race_id) as times_ran,sum(if(gdp.position = 1,1,0)) "
				+ "as position1,sum(if(gdp.position = 2,1,0)) as position2,sum(if(gdp.position = 3,1,0)) "
				+ "as position3,sum(if(gdp.position = 4,1,0)) as position4,sum(if(gdp.position = 5,1,0)) "
				+ "as position5,sum(if(gdp.position = 6,1,0)) as position6,sum(gdp.prize) as prize_money from greyhound gd "
				+ "join greyhound_profile gdp on gdp.greyhound_id = gd.greyhound_id "
				+ "join race r on r.race_id = gdp.race_id where ";
		Calendar c = Calendar.getInstance();
		Date fromDate = filterWithPagination.getFilter().getFromDate();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);
		fromDate = c.getTime();

		Date toDate = filterWithPagination.getFilter().getToDate();
		c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		toDate = c.getTime();
		query += "gdp.date between '" + Utility.getFormatedDateFromDate(fromDate) + " 00:00:00' and '"
				+ Utility.getFormatedDateFromDate(toDate) + " 00:00:00'";

		boolean flag = true;
		boolean whereFlag = false;

		if (filterWithPagination.getFilter().getTrainerId() != 0) {
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.trainer_id = " + filterWithPagination.getFilter().getTrainerId();
			flag = true;
			whereFlag = false;
		}
		if (filterWithPagination.getFilter().getTrack() != null
				&& !filterWithPagination.getFilter().getTrack().isEmpty()
				&& !filterWithPagination.getFilter().getTrack().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.track = '" + filterWithPagination.getFilter().getTrack() + "'";
			flag = true;
			whereFlag = false;
		}
		if (filterWithPagination.getFilter().getGrade() != null
				&& !filterWithPagination.getFilter().getGrade().isEmpty()
				&& !filterWithPagination.getFilter().getGrade().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.race_class = '"
					+ filterWithPagination.getFilter().getGrade() + "'";
			flag = true;
			whereFlag = false;
		}

		if (filterWithPagination.getFilter().getDistance() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance >= "
					+ filterWithPagination.getFilter().getDistance();
			flag = true;
			whereFlag = false;
		}
		query += " group by gd.name";
		Query queryString = entityManager.createNativeQuery(query);
		int totalCounts = queryString.getResultList().size();
		PaginationDataDto paginationDataDto = Utility.getPaginationData(totalCounts,
				filterWithPagination.getPagination());
		queryString = entityManager.createNativeQuery(query).setFirstResult(paginationDataDto.getFrom())
				.setMaxResults(paginationDataDto.getTo());
		List<Object[]> objectList = queryString.getResultList();
		for (Object[] object : objectList) {
			TrainerResponseDto trainerResponseDto = new TrainerResponseDto();
			trainerResponseDto.setGreyhoundName((String) object[0]);
			trainerResponseDto.setTimesRan(object[1].toString());
			trainerResponseDto.setPosition1(object[2].toString());
			trainerResponseDto.setPosition2(object[3].toString());
			trainerResponseDto.setPosition3(object[4].toString());
			trainerResponseDto.setPosition4(object[5].toString());
			trainerResponseDto.setPosition5(object[6].toString());
			trainerResponseDto.setPosition6(object[7].toString());
			trainerResponseDto.setPrizeMoney(object[8] != null ? Double.parseDouble(object[8].toString()) : 0);
			dataList.add(trainerResponseDto);
		}
		filterWithPagination.getPagination().setData(dataList);
		filterWithPagination.getPagination().setTotalCount(totalCounts);
		filterWithPagination.getPagination().setTotalPages(paginationDataDto.getTotalPages());
		return filterWithPagination.getPagination();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrainerResponseDto> exportTrainers(TrainerFilterDto filter) {
		List<TrainerResponseDto> dataList = new ArrayList<>();
		String query = "select gd.name as greyhound_name,count(r.race_id) as times_ran,sum(if(gdp.position = 1,1,0)) "
				+ "as position1,sum(if(gdp.position = 2,1,0)) as position2,sum(if(gdp.position = 3,1,0)) "
				+ "as position3,sum(if(gdp.position = 4,1,0)) as position4,sum(if(gdp.position = 5,1,0)) "
				+ "as position5,sum(if(gdp.position = 6,1,0)) as position6,sum(gdp.prize) as prize_money from greyhound gd "
				+ "join greyhound_profile gdp on gdp.greyhound_id = gd.greyhound_id "
				+ "join race r on r.race_id = gdp.race_id where ";
		Calendar c = Calendar.getInstance();
		Date fromDate = filter.getFromDate();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);
		fromDate = c.getTime();

		Date toDate = filter.getToDate();
		c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		toDate = c.getTime();
		query += "gdp.date between '" + Utility.getFormatedDateFromDate(fromDate) + " 00:00:00' and '"
				+ Utility.getFormatedDateFromDate(toDate) + " 00:00:00'";
		boolean flag = true;
		boolean whereFlag = false;

		if (filter.getTrainerId() != 0) {
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.trainer_id = " + filter.getTrainerId();
			flag = true;
			whereFlag = false;
		}
		if (filter.getTrack() != null && !filter.getTrack().isEmpty() && !filter.getTrack().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.track = '" + filter.getTrack() + "'";
			flag = true;
			whereFlag = false;
		}
		if (filter.getGrade() != null && !filter.getGrade().isEmpty() && !filter.getGrade().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.race_class = '" + filter.getGrade()
					+ "'";
			flag = true;
			whereFlag = false;
		}
		if (filter.getDistance() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance >= " + filter.getDistance();
			flag = true;
			whereFlag = false;
		}
		query += " group by gd.name";
		Query queryString = entityManager.createNativeQuery(query);
		List<Object[]> objectList = queryString.getResultList();
		for (Object[] object : objectList) {
			TrainerResponseDto trainerResponseDto = new TrainerResponseDto();
			trainerResponseDto.setGreyhoundName((String) object[0]);
			trainerResponseDto.setTimesRan(object[1].toString());
			trainerResponseDto.setPosition1(object[2].toString());
			trainerResponseDto.setPosition2(object[3].toString());
			trainerResponseDto.setPosition3(object[4].toString());
			trainerResponseDto.setPosition4(object[5].toString());
			trainerResponseDto.setPosition5(object[6].toString());
			trainerResponseDto.setPosition6(object[7].toString());
			trainerResponseDto.setPrizeMoney(object[8] != null ? Double.parseDouble(object[8].toString()) : 0);
			dataList.add(trainerResponseDto);
		}
		return dataList;
	}

	@Override
	public Map<String, Map<String, String>> getTrainerGraphData(TrainerFilterDto filter) {
		Map<String, Map<String, String>> map = new LinkedHashMap<>();
		Calendar calFor7Days = Calendar.getInstance();
		calFor7Days.setTime(new Date());
		Date daysFor7EndDate = calFor7Days.getTime();
		calFor7Days.add(Calendar.DATE, -7);
		Date daysFor7StartDate = calFor7Days.getTime();
		map.put("7 days", getGraphData(filter, daysFor7StartDate, daysFor7EndDate, false));

		Calendar calFor30Days = Calendar.getInstance();
		calFor30Days.setTime(new Date());
		Date daysFor30EndDate = calFor30Days.getTime();
		calFor30Days.add(Calendar.DATE, -30);
		Date daysFor30StartDate = calFor30Days.getTime();
		map.put("30 days", getGraphData(filter, daysFor30StartDate, daysFor30EndDate, false));

		Calendar calFor120Days = Calendar.getInstance();
		calFor120Days.setTime(new Date());
		Date daysFor120EndDate = calFor120Days.getTime();
		calFor120Days.add(Calendar.DATE, -120);
		Date daysFor120StartDate = calFor120Days.getTime();
		map.put("120 days", getGraphData(filter, daysFor120StartDate, daysFor120EndDate, false));

		Calendar calFor1yearDays = Calendar.getInstance();
		calFor1yearDays.setTime(new Date());
		Date daysFor1yearEndDate = calFor1yearDays.getTime();
		calFor1yearDays.add(Calendar.DATE, -365);
		Date daysFor1yearStartDate = calFor1yearDays.getTime();
		map.put("365 Days", getGraphData(filter, daysFor1yearStartDate, daysFor1yearEndDate, false));

		map.put("Life Time", getGraphData(filter, null, null, true));
		return map;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getGraphData(TrainerFilterDto filter, Date fromDate, Date toDate, boolean isLifeTime) {
		Map<String, String> map = new LinkedHashMap<>();
//		String query = "select sum(gdp.prize),sum(if(gdp.position = 1,1,0)) from greyhound_profile gdp "
		String query = "select sum(gdp.prize),avg(gdp.rating),sum(if(gdp.position = 1,1,0)) from greyhound_profile gdp "
				+ "join greyhound gd on gd.greyhound_id = gdp.greyhound_id " + "join race r on r.race_id = gdp.race_id";
		query += " where gd.trainer_id = " + filter.getTrainerId() + " and gd.track = '" + filter.getTrack() + "'";
		if (!isLifeTime) {
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			fromDate = c.getTime();

			c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			toDate = c.getTime();
			query += " and gdp.date between '" + Utility.getFormatedDateFromDate(fromDate) + " 00:00:00' and '"
					+ Utility.getFormatedDateFromDate(toDate) + " 00:00:00'";
		}

		boolean flag = true;
		boolean whereFlag = false;
		if (filter.getGrade() != null && !filter.getGrade().isEmpty() && !filter.getGrade().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.race_class = '" + filter.getGrade()
					+ "'";
			flag = true;
			whereFlag = false;
		}

		if (filter.getDistance() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance >= " + filter.getDistance();
			flag = true;
			whereFlag = false;
		}
		Query queryString = entityManager.createNativeQuery(query);
		List<Object[]> objectList = queryString.getResultList();
		if (!objectList.isEmpty()) {
			for (Object[] object : objectList) {
				map.put("prize", object[0] != null ? object[0].toString() : "0");
				map.put("rating",
						object[1] != null ? String.format("%.2f", Double.parseDouble(object[1].toString())) : "0");
				map.put("win", object[2] != null ? object[2].toString() : "0");
			}
		} else {
			map.put("prize", "0");
			map.put("rating", "0");
			map.put("win", "0");
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Trainer> getTrainersByTrack(String track) {
		List<Trainer> trainerList = new ArrayList<>();
		String query = "select tr.id,tr.name from Trainer tr join Greyhound gd on tr.id = gd.trainerId where gd.track = '"
				+ track + "' group by tr.name, tr.id";
		Query queryString = null;
		try {
			queryString = entityManager.createQuery(query);
		} catch (Exception e) {
			System.out.println("Exception in create query" + e.getMessage());
		}
		List<Object[]> dataList = new ArrayList<>();
		try {
			dataList = queryString.getResultList();
		} catch (Exception e) {
			System.out.println("Exception in get data" + e.getMessage());
		}
		for (Object[] objects : dataList) {
			Trainer trainer = new Trainer();
			trainer.setId((Long) objects[0]);
			trainer.setName((String) objects[1]);
			trainerList.add(trainer);
		}
		return trainerList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Trainer> getTrainersByTracks(List<String> tracks) {
		List<Trainer> trainerList = new ArrayList<>();
		String tracksString = "(";
		for (String track : tracks) {
			tracksString += "'" + track + "',";
		}
		tracksString = tracksString.substring(0, tracksString.length() - 1) + ")";
		String query = "select tr.id,tr.name from Trainer tr join Greyhound gd on tr.id = gd.trainerId where gd.track in "
				+ tracksString + " group by tr.id,tr.name";
		Query queryString = null;
		try {
			queryString = entityManager.createQuery(query);
		} catch (Exception e) {
			System.out.println("Exception in create query" + e.getMessage());
		}
		List<Object[]> dataList = new ArrayList<>();
		try {
			dataList = queryString.getResultList();
		} catch (Exception e) {
			System.out.println("Exception in get data" + e.getMessage());
		}
		for (Object[] objects : dataList) {
			Trainer trainer = new Trainer();
			trainer.setId((Long) objects[0]);
			trainer.setName((String) objects[1]);
			trainerList.add(trainer);
		}
		return trainerList;
	}

	@Override
	public Map<String, Map<String, String>> compareTrainer(TrainerCompareRequestDto trainerCompare) {
		Map<String, Map<String, String>> map = new LinkedHashMap<>();
		if (trainerCompare.getTrainer() != null) {
			for (Long trainer : trainerCompare.getTrainer()) {
				Trainer trainerEntity = trainerService.findById(trainer);
				map.put(trainerEntity.getName(),
						getCompairGraphDataByDays(trainerCompare.getTrack(), trainer, trainerCompare.getDays()));
			}
		}
		return map;
	}

	private Map<String, String> getCompairGraphDataByDays(String track, Long trainerId, int days) {
		Map<String, String> map = new LinkedHashMap<>();
		if (days == 7) {
			Calendar calFor7Days = Calendar.getInstance();
			calFor7Days.setTime(new Date());
			Date daysFor7EndDate = calFor7Days.getTime();
			calFor7Days.add(Calendar.DATE, -7);
			Date daysFor7StartDate = calFor7Days.getTime();
			map = getTrainerCompareGraphData(trainerId, track, daysFor7StartDate, daysFor7EndDate, false);
		}

		if (days == 30) {
			Calendar calFor30Days = Calendar.getInstance();
			calFor30Days.setTime(new Date());
			Date daysFor30EndDate = calFor30Days.getTime();
			calFor30Days.add(Calendar.DATE, -30);
			Date daysFor30StartDate = calFor30Days.getTime();
			map = getTrainerCompareGraphData(trainerId, track, daysFor30StartDate, daysFor30EndDate, false);
		}

		if (days == 60) {
			Calendar calFor60Days = Calendar.getInstance();
			calFor60Days.setTime(new Date());
			Date daysFor60EndDate = calFor60Days.getTime();
			calFor60Days.add(Calendar.DATE, -60);
			Date daysFor60StartDate = calFor60Days.getTime();
			map = getTrainerCompareGraphData(trainerId, track, daysFor60StartDate, daysFor60EndDate, false);
		}

		if (days == 90) {
			Calendar calFor90Days = Calendar.getInstance();
			calFor90Days.setTime(new Date());
			Date daysFor90EndDate = calFor90Days.getTime();
			calFor90Days.add(Calendar.DATE, -90);
			Date daysFor90StartDate = calFor90Days.getTime();
			map = getTrainerCompareGraphData(trainerId, track, daysFor90StartDate, daysFor90EndDate, false);
		}

		if (days == 120) {
			Calendar calFor120Days = Calendar.getInstance();
			calFor120Days.setTime(new Date());
			Date daysFor120EndDate = calFor120Days.getTime();
			calFor120Days.add(Calendar.DATE, -120);
			Date daysFor120StartDate = calFor120Days.getTime();
			map = getTrainerCompareGraphData(trainerId, track, daysFor120StartDate, daysFor120EndDate, false);
		}

		if (days == 180) {
			Calendar calFor180Days = Calendar.getInstance();
			calFor180Days.setTime(new Date());
			Date daysFor180EndDate = calFor180Days.getTime();
			calFor180Days.add(Calendar.DATE, -180);
			Date daysFor180StartDate = calFor180Days.getTime();
			map = getTrainerCompareGraphData(trainerId, track, daysFor180StartDate, daysFor180EndDate, false);
		}

		if (days == 365) {
			Calendar calFor1yearDays = Calendar.getInstance();
			calFor1yearDays.setTime(new Date());
			Date daysFor1yearEndDate = calFor1yearDays.getTime();
			calFor1yearDays.add(Calendar.DATE, -365);
			Date daysFor1yearStartDate = calFor1yearDays.getTime();
			map = getTrainerCompareGraphData(trainerId, track, daysFor1yearStartDate, daysFor1yearEndDate, false);
		}
		if (days == 1) {
			map = getTrainerCompareGraphData(trainerId, track, null, null, true);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getTrainerCompareGraphData(Long trainer, String track, Date fromDate, Date toDate,
			boolean isLifeTime) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		String query = "select sum(gdp.prize),sum(if(gdp.position = 1,1,0)) from greyhound_profile gdp "
				+ "join greyhound gd on gdp.greyhound_id = gd.greyhound_id " + "join race r on r.race_id = gdp.race_id";
		query += " where gd.trainer_id = " + trainer + " and gd.track = '" + track + "'";
		if (!isLifeTime) {
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			fromDate = c.getTime();

			c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			toDate = c.getTime();
			query += " and gdp.date between '" + Utility.getFormatedDateFromDate(fromDate) + " 00:00:00' and '"
					+ Utility.getFormatedDateFromDate(toDate) + " 00:00:00'";
		}
		Query queryString = entityManager.createNativeQuery(query);
		List<Object[]> greyhoundList = queryString.getResultList();
		if (!greyhoundList.isEmpty()) {
			for (Object[] object : greyhoundList) {
				map.put("prize", object[0] != null ? object[0].toString() : "0");
				map.put("win", object[1] != null ? object[1].toString() : "0");
			}
		} else {
			map.put("prize", "0");
			map.put("win", "0");
		}
		return map;
	}

	@Override
	public List<Trainer> getTrainersByDateAndTrack(TrainerByDateAndTrackRequestDto trainerByDateAndTrackRequestDto) {
		List<Trainer> trainerList = new ArrayList<>();
		String query = "select distinct(tr.id),tr.name from Trainer tr " + "join Greyhound gd on tr.id = gd.trainerId "
				+ "join GreyhoundProfile gdp on gdp.greyhoundId = gd.greyhoundId where gd.track in ";
		String tracksString = "(";
		for (String track : trainerByDateAndTrackRequestDto.getTrack()) {
			tracksString += "'" + track + "',";
		}
		tracksString = tracksString.substring(0, tracksString.length() - 1) + ")";
		query += tracksString;
		Calendar c = Calendar.getInstance();
		Date fromDate = trainerByDateAndTrackRequestDto.getFromDate();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);
		fromDate = c.getTime();

		Date toDate = trainerByDateAndTrackRequestDto.getToDate();
		c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		toDate = c.getTime();
		query += " and gdp.date between '" + Utility.getFormatedDateFromDate(fromDate) + " 00:00:00' and '"
				+ Utility.getFormatedDateFromDate(toDate) + " 00:00:00'";
		query += " group by tr.name, tr.id";
		Query queryString = null;
		try {
			queryString = entityManager.createQuery(query);
		} catch (Exception e) {
			System.out.println("Exception in create query" + e.getMessage());
		}
		List<Object[]> dataList = new ArrayList<>();
		try {
			dataList = queryString.getResultList();
		} catch (Exception e) {
			System.out.println("Exception in get data" + e.getMessage());
		}
		for (Object[] objects : dataList) {
//			Trainer trainerExist = trainerList.stream().filter(user -> objects[0].toString() == user.getId().toString())
//					.findFirst().orElse(null);
//			if (trainerExist == null) {
			Trainer trainer = new Trainer();
			trainer.setId((Long) objects[0]);
			trainer.setName((String) objects[1]);
			trainerList.add(trainer);
//			}
		}
		return trainerList;
	}
}
