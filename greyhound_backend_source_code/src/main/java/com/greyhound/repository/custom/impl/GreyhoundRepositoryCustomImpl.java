package com.greyhound.repository.custom.impl;

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

import com.greyhound.dto.GreyhoundByDateAndTrackAndTrainersRequestDto;
import com.greyhound.dto.GreyhoundCompareRequestDto;
import com.greyhound.dto.GreyhoundCompareResponseDto;
import com.greyhound.dto.GreyhoundFilterDto;
import com.greyhound.dto.GreyhoundFilterWithPagination;
import com.greyhound.dto.GreyhoundReportResponseDto;
import com.greyhound.dto.GreyhoundWithTrainerResponseDto;
import com.greyhound.dto.PaginationDataDto;
import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.ReportFilterDtoWithPagination;
import com.greyhound.dto.ReportRequestDto;
import com.greyhound.dto.ReportResponseDto;
import com.greyhound.model.Greyhound;
import com.greyhound.model.Trainer;
import com.greyhound.repository.custom.GreyhoundRepositoryCustom;
import com.greyhound.service.ITrainerService;
import com.greyhound.utility.Utility;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public class GreyhoundRepositoryCustomImpl implements GreyhoundRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private ITrainerService trainerService;

	@SuppressWarnings("unchecked")
	@Override
	public PaginationDto getGreyhoundByFilterWithPagination(GreyhoundFilterWithPagination filterWithPagination) {
		List<GreyhoundReportResponseDto> dataList = new ArrayList<>();
		String query = "select gdp.date,gd.track,r.distance,r.raceClass,gdp.stmhcp,gdp.position,"
				+ "gdp.winTime,gdp.weight,gdp.calcTm,gdp.rating,gdp.prize,gdp.remarks,gdp.id "
				+ "from GreyhoundProfile gdp join Greyhound gd on gdp.greyhoundId = gd.greyhoundId join Race r on r.raceId = gdp.raceId where ";
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
		if (filterWithPagination.getFilter().getTrack() != null
				&& !filterWithPagination.getFilter().getTrack().isEmpty()) {
			query += Utility.addWhere(whereFlag);
			String trackIds = "(";
			for (String track : filterWithPagination.getFilter().getTrack()) {
				trackIds += "'" + track + "',";
			}
			trackIds = trackIds.substring(0, trackIds.length() - 1) + ")";
			query += Utility.addANDOrOR(flag) + " gd.track in " + trackIds;
			flag = true;
			whereFlag = false;
		}

		if (filterWithPagination.getFilter().getTrainerId() != null
				&& filterWithPagination.getFilter().getTrainerId() != 0) {
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.trainerId = " + filterWithPagination.getFilter().getTrainerId();
			flag = true;
			whereFlag = false;
		}
		if (filterWithPagination.getFilter().getGreyhoundId() != null
				&& filterWithPagination.getFilter().getGreyhoundId() != 0) {
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.greyhoundId = "
					+ filterWithPagination.getFilter().getGreyhoundId();
			flag = true;
			whereFlag = false;
		}
		if (filterWithPagination.getFilter().getGrade() != null
				&& !filterWithPagination.getFilter().getGrade().isEmpty()
				&& !filterWithPagination.getFilter().getGrade().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.raceClass = '"
					+ filterWithPagination.getFilter().getGrade() + "'";
			flag = true;
			whereFlag = false;
		}

		if (filterWithPagination.getFilter().getDistance() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance = "
					+ filterWithPagination.getFilter().getDistance();
			flag = true;
			whereFlag = false;
		}
		Query queryString = entityManager.createQuery(query);
		int totalCounts = queryString.getResultList().size();
		PaginationDataDto paginationDataDto = Utility.getPaginationData(totalCounts,
				filterWithPagination.getPagination());
		query += "order by gdp.date desc";
		queryString = entityManager.createQuery(query).setFirstResult(paginationDataDto.getFrom())
				.setMaxResults(paginationDataDto.getTo());
		List<Object[]> greyhoundList = queryString.getResultList();
		for (Object[] object : greyhoundList) {
			GreyhoundReportResponseDto greyhoundReportResponseDto = new GreyhoundReportResponseDto();
			greyhoundReportResponseDto.setDate(object[0].toString().split(" ")[0]);
			greyhoundReportResponseDto.setTrack((String) object[1]);
			greyhoundReportResponseDto.setDistance((int) object[2]);
			greyhoundReportResponseDto.setGrade((String) object[3]);
			greyhoundReportResponseDto.setsTmHcp(
					object[4] != null && !object[4].toString().equals("-") ? Double.parseDouble(object[4].toString())
							: 0.0);
			greyhoundReportResponseDto.setPosition((int) object[5]);
			greyhoundReportResponseDto
					.setWinTime(!object[6].toString().equals("-") ? Double.parseDouble(object[6].toString()) : 0.0);
			greyhoundReportResponseDto
					.setWeight(!object[7].toString().equals("-") ? Double.parseDouble(object[7].toString()) : 0.0);
			greyhoundReportResponseDto.setCalcTm(
					object[8] != null && !object[8].toString().equals("-") ? Double.parseDouble(object[8].toString())
							: 0.0);
			greyhoundReportResponseDto.setRating(object[9] != null ? object[9].toString() : "0");
			greyhoundReportResponseDto.setPrizeMoney(
					object[10] != null && !object[10].toString().equals("-") ? Double.parseDouble(object[10].toString())
							: 0.0);
			greyhoundReportResponseDto.setComment((String) object[11]);
			greyhoundReportResponseDto.setId((Long) object[12]);
			dataList.add(greyhoundReportResponseDto);
		}
		filterWithPagination.getPagination().setData(dataList);
		filterWithPagination.getPagination().setTotalCount(totalCounts);
		filterWithPagination.getPagination().setTotalPages(paginationDataDto.getTotalPages());
		return filterWithPagination.getPagination();
	}

	@Override
	public List<GreyhoundReportResponseDto> exportGreyhounds(GreyhoundFilterDto filter) {
		List<GreyhoundReportResponseDto> dataList = new ArrayList<>();
		String query = "select gdp.date,gd.track,r.distance,r.raceClass,gdp.stmhcp,gdp.position,"
				+ "gdp.winTime,gdp.weight,gdp.calcTm,gdp.rating,gdp.prize,gdp.remarks"
				+ " from GreyhoundProfile gdp join Greyhound gd on gdp.greyhoundId = gd.greyhoundId join Race r on r.raceId = gdp.raceId where ";
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
		if (filter.getTrack() != null && !filter.getTrack().isEmpty()) {
			query += Utility.addWhere(whereFlag);
			String trackIds = "(";
			for (String track : filter.getTrack()) {
				trackIds += "'" + track + "',";
			}
			trackIds = trackIds.substring(0, trackIds.length() - 1) + ")";
			query += Utility.addANDOrOR(flag) + " gd.track in " + trackIds;
			flag = true;
			whereFlag = false;
		}

		if (filter.getTrainerId() != null && filter.getTrainerId() != 0) {
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.trainerId = " + filter.getTrainerId();
			flag = true;
			whereFlag = false;
		}
		if (filter.getGreyhoundId() != null && filter.getGreyhoundId() != 0) {
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.greyhoundId = " + filter.getGreyhoundId();
			flag = true;
			whereFlag = false;
		}
		if (filter.getGrade() != null && !filter.getGrade().isEmpty() && !filter.getGrade().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.raceClass = '" + filter.getGrade()
					+ "'";
			flag = true;
			whereFlag = false;
		}

		if (filter.getDistance() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance = " + filter.getDistance();
			flag = true;
			whereFlag = false;
		}
		Query queryString = entityManager.createQuery(query);
		List<Object[]> greyhoundList = queryString.getResultList();
		for (Object[] object : greyhoundList) {
			GreyhoundReportResponseDto greyhoundReportResponseDto = new GreyhoundReportResponseDto();
			greyhoundReportResponseDto.setDate(object[0].toString().split(" ")[0]);
			greyhoundReportResponseDto.setTrack((String) object[1]);
			greyhoundReportResponseDto.setDistance((int) object[2]);
			greyhoundReportResponseDto.setGrade((String) object[3]);
			greyhoundReportResponseDto.setsTmHcp(
					object[4] != null && !object[4].toString().equals("-") ? Double.parseDouble(object[4].toString())
							: 0.0);
			greyhoundReportResponseDto.setPosition((int) object[5]);
			greyhoundReportResponseDto
					.setWinTime(!object[6].toString().equals("-") ? Double.parseDouble(object[6].toString()) : 0.0);
			greyhoundReportResponseDto
					.setWeight(!object[7].toString().equals("-") ? Double.parseDouble(object[7].toString()) : 0.0);
			greyhoundReportResponseDto
					.setCalcTm(!object[8].toString().equals("-") ? Double.parseDouble(object[8].toString()) : 0.0);
			greyhoundReportResponseDto.setRating(object[9] != null ? object[9].toString() : "0");
			greyhoundReportResponseDto.setPrizeMoney(
					object[10] != null && !object[10].toString().equals("-") ? Double.parseDouble(object[10].toString())
							: 0.0);
			greyhoundReportResponseDto.setComment((String) object[11]);
			dataList.add(greyhoundReportResponseDto);
		}
		return dataList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GreyhoundCompareResponseDto> compareGreyhound(GreyhoundCompareRequestDto greyhoundCompare) {
		List<GreyhoundCompareResponseDto> dataList = new ArrayList<>();
		String query = "select gd.name,avg(gdp.rating),avg(gdp.stmhcp),avg(gdp.calcTm),avg(gdp.winTime)"
				+ " from Greyhound gd join GreyhoundProfile gdp on gd.greyhoundId = gdp.greyhoundId "
				+ "join Race r on r.raceId = gdp.raceId where ";
		Calendar c = Calendar.getInstance();
		Date fromDate = greyhoundCompare.getFromDate();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);
		fromDate = c.getTime();

		Date toDate = greyhoundCompare.getToDate();
		c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		toDate = c.getTime();
		query += "gdp.date between '" + Utility.getFormatedDateFromDate(fromDate) + " 00:00:00' and '"
				+ Utility.getFormatedDateFromDate(toDate) + " 00:00:00'";

		boolean flag = true;
		boolean whereFlag = false;
		if (greyhoundCompare.getTrack() != null && !greyhoundCompare.getTrack().isEmpty()
				&& !greyhoundCompare.getTrack().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.track = '"
					+ greyhoundCompare.getTrack() + "'";
			flag = true;
			whereFlag = false;
		}

		if (greyhoundCompare.getTrainer() != null && !greyhoundCompare.getTrainer().isEmpty()) {
			String trainerIds = "(";
			for (Long trainerId : greyhoundCompare.getTrainer()) {
				trainerIds += trainerId + ",";
			}
			trainerIds = trainerIds.substring(0, trainerIds.length() - 1) + ")";
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.trainerId in " + trainerIds;
			flag = true;
			whereFlag = false;
		}

		if (greyhoundCompare.getGreyhound() != null && !greyhoundCompare.getGreyhound().isEmpty()) {
			String ids = "(";
			for (Long id : greyhoundCompare.getGreyhound()) {
				ids += id + ",";
			}
			ids = ids.substring(0, ids.length() - 1) + ")";
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.greyhoundId in " + ids;
			flag = true;
			whereFlag = false;
		}

		if (greyhoundCompare.getDistance() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance = "
					+ greyhoundCompare.getDistance();
			flag = true;
			whereFlag = false;
		}

		query += " group by gd.name";

		Query queryString = entityManager.createQuery(query);
		List<Object[]> greyhoundList = queryString.getResultList();
		for (Object[] object : greyhoundList) {
			GreyhoundCompareResponseDto reportResponseDto = new GreyhoundCompareResponseDto();
			reportResponseDto.setGreyhoundName((String) object[0]);
			reportResponseDto.setRating(object[1] != null ? (double) object[1] : 0.0);
			reportResponseDto.setStmhcp(object[2] != null ? Double.parseDouble(object[2].toString()) : 0);
			reportResponseDto.setCalcTime((double) object[3]);
			reportResponseDto.setWinTime(Double.parseDouble(object[4].toString()));
			dataList.add(reportResponseDto);
		}
		return dataList;
	}

	@Override
	public Object exportReport(ReportRequestDto reportRequestDto) {
		List<ReportResponseDto> dataList = new ArrayList<>();
		String query = "select gd.name,count(gdp.id),avg(gdp.rating),avg(gdp.stmhcp),avg(gdp.winTime),"
				+ "avg(gdp.weight),avg(gdp.calcTm) from GreyhoundProfile gdp "
				+ "join Greyhound gd on gdp.greyhoundId = gd.greyhoundId "
				+ "join Race r on r.raceId = gdp.raceId where ";
		Calendar c = Calendar.getInstance();
		Date fromDate = reportRequestDto.getFromDate();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);
		fromDate = c.getTime();

		Date toDate = reportRequestDto.getToDate();
		c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		toDate = c.getTime();
		query += "gdp.date between '" + Utility.getFormatedDateFromDate(fromDate) + " 00:00:00' and '"
				+ Utility.getFormatedDateFromDate(toDate) + " 00:00:00'";

		boolean flag = true;
		boolean whereFlag = false;
		if (reportRequestDto.getTrack() != null && !reportRequestDto.getTrack().isEmpty()
				&& !reportRequestDto.getTrack().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.track like '%"
					+ reportRequestDto.getTrack() + "%'";
			flag = true;
			whereFlag = false;
		}

		if (reportRequestDto.getTrainer() != null && reportRequestDto.getTrainer() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.trainerId = "
					+ reportRequestDto.getTrainer();
			flag = true;
			whereFlag = false;
		}

		if (reportRequestDto.getGrade() != null && !reportRequestDto.getGrade().isEmpty()
				&& !reportRequestDto.getGrade().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.raceClass = '"
					+ reportRequestDto.getGrade() + "'";
			flag = true;
			whereFlag = false;
		}

		if (reportRequestDto.getDistance() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance = "
					+ reportRequestDto.getDistance();
			flag = true;
			whereFlag = false;
		}

		query += " group by gd.name";

		Query queryString = entityManager.createQuery(query);
		List<Object[]> greyhoundList = queryString.getResultList();
		for (Object[] object : greyhoundList) {
			ReportResponseDto reportResponseDto = new ReportResponseDto();
			reportResponseDto.setGreyhoundName((String) object[0]);
			reportResponseDto.setEntryNum((long) object[1]);
			reportResponseDto.setRating(object[2] != null ? Double.parseDouble(object[2].toString()) : 0);
			reportResponseDto.setStmhcp(object[3] != null ? Double.parseDouble(object[3].toString()) : 0);
			reportResponseDto.setWinTime(object[4] != null ? object[4].toString() : "0");
			reportResponseDto.setWeight(object[5] != null ? Double.parseDouble(object[5].toString()) : 0);
			reportResponseDto.setCalcTm(object[6] != null ? Double.parseDouble(object[6].toString()) : 0);
			// reportResponseDto.setNote(object[7] != null ? (String) object[7] : "");
			dataList.add(reportResponseDto);
		}
		return dataList;
	}

	@Override
	public Map<String, Map<String, String>> greyhoundGraphData(GreyhoundFilterDto filter) {
		Map<String, Map<String, String>> map = new LinkedHashMap<>();
		Calendar calFor7Days = Calendar.getInstance();
		calFor7Days.setTime(new Date());
		Date daysFor7EndDate = calFor7Days.getTime();
		calFor7Days.add(Calendar.DATE, -7);
		Date daysFor7StartDate = calFor7Days.getTime();
		map.put("7 days", getGreyhoundGraphData(filter, daysFor7StartDate, daysFor7EndDate, false));

		Calendar calFor30Days = Calendar.getInstance();
		calFor30Days.setTime(new Date());
		Date daysFor30EndDate = calFor30Days.getTime();
		calFor30Days.add(Calendar.DATE, -30);
		Date daysFor30StartDate = calFor30Days.getTime();
		map.put("30 days", getGreyhoundGraphData(filter, daysFor30StartDate, daysFor30EndDate, false));

		Calendar calFor120Days = Calendar.getInstance();
		calFor120Days.setTime(new Date());
		Date daysFor120EndDate = calFor120Days.getTime();
		calFor120Days.add(Calendar.DATE, -120);
		Date daysFor120StartDate = calFor120Days.getTime();
		map.put("120 days", getGreyhoundGraphData(filter, daysFor120StartDate, daysFor120EndDate, false));

		Calendar calFor1yearDays = Calendar.getInstance();
		calFor1yearDays.setTime(new Date());
		Date daysFor1yearEndDate = calFor1yearDays.getTime();
		calFor1yearDays.add(Calendar.DATE, -365);
		Date daysFor1yearStartDate = calFor1yearDays.getTime();
		map.put("365 Days", getGreyhoundGraphData(filter, daysFor1yearStartDate, daysFor1yearEndDate, false));

		map.put("Life Time", getGreyhoundGraphData(filter, null, null, true));
		return map;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getGreyhoundGraphData(GreyhoundFilterDto filter, Date fromDate, Date toDate,
			boolean isLifeTime) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		String trackIds = "(";
		for (String track : filter.getTrack()) {
			trackIds += "'" + track + "',";
		}
		trackIds = trackIds.substring(0, trackIds.length() - 1) + ")";
		String query = "select sum(gdp.prize),avg(gdp.rating),sum(if(gdp.position = 1,1,0)) from greyhound_profile gdp "
				+ "join greyhound gd on gdp.greyhound_id = gd.greyhound_id join race r on r.race_id = gdp.race_id";
		query += " where gdp.greyhound_id = " + filter.getGreyhoundId() + " and gd.trainer_id = "
				+ filter.getTrainerId() + " and gd.track in " + trackIds;
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
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance = " + filter.getDistance();
			flag = true;
			whereFlag = false;
		}
		Query queryString = entityManager.createNativeQuery(query);
		List<Object[]> greyhoundList = queryString.getResultList();
		if (!greyhoundList.isEmpty()) {
			for (Object[] object : greyhoundList) {
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
	public PaginationDto getGreyhoundDataByFilter(ReportFilterDtoWithPagination filterWithPagination) {
		List<ReportResponseDto> dataList = new ArrayList<>();
//		String query = "select gd.name,count(gdp.id),avg(gdp.rating),avg(gdp.stmhcp),avg(gdp.winTime),"
//				+ "avg(gdp.weight),avg(gdp.calcTm),gdp.remarks from GreyhoundProfile gdp "
//				+ "join Greyhound gd on gdp.greyhoundId = gd.greyhoundId "
//				+ "join Race r on r.raceId = gdp.raceId where ";
		String query = "select gd.name,count(gdp.id),avg(gdp.rating),avg(gdp.stmhcp),avg(gdp.winTime),"
				+ "avg(gdp.weight),avg(gdp.calcTm),sum(gdp.prize),gd.greyhoundId from GreyhoundProfile gdp "
				+ "join Greyhound gd on gdp.greyhoundId = gd.greyhoundId "
				+ "join Race r on r.raceId = gdp.raceId where ";

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
		if (filterWithPagination.getFilter().getTrack() != null
				&& !filterWithPagination.getFilter().getTrack().isEmpty()
				&& !filterWithPagination.getFilter().getTrack().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.track like '%"
					+ filterWithPagination.getFilter().getTrack() + "%'";
			flag = true;
			whereFlag = false;
		}

		if (filterWithPagination.getFilter().getTrainer() != null
				&& filterWithPagination.getFilter().getTrainer() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.trainerId = "
					+ filterWithPagination.getFilter().getTrainer();
			flag = true;
			whereFlag = false;
		}

		if (filterWithPagination.getFilter().getGrade() != null
				&& !filterWithPagination.getFilter().getGrade().isEmpty()
				&& !filterWithPagination.getFilter().getGrade().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.raceClass = '"
					+ filterWithPagination.getFilter().getGrade() + "'";
			flag = true;
			whereFlag = false;
		}

		if (filterWithPagination.getFilter().getDistance() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " r.distance = "
					+ filterWithPagination.getFilter().getDistance();
			flag = true;
			whereFlag = false;
		}

//		query += " group by gd.name,gdp.remarks";
		query += " group by gd.name,gd.greyhoundId";

		Query queryString = entityManager.createQuery(query);
		int totalCounts = queryString.getResultList().size();
		PaginationDataDto paginationDataDto = Utility.getPaginationData(totalCounts,
				filterWithPagination.getPagination());
//		if (filterWithPagination.getFilter().getOrder() != null
//				&& !filterWithPagination.getFilter().getOrder().isEmpty()) {
//			if (filterWithPagination.getFilter().getOrder().equals("rank")) {
//				query += ",gdp.rating order by gdp.rating asc";
//			} else if (filterWithPagination.getFilter().getOrder().equals("stmhcp")) {
//				query += ",gdp.stmhcp order by gdp.stmhcp asc";
//			} else if (filterWithPagination.getFilter().getOrder().equals("calctm")) {
//				query += ",gdp.calcTm order by gdp.calcTm asc";
//			} else if (filterWithPagination.getFilter().getOrder().equals("wintime")) {
//				query += ",gdp.winTime order by gdp.winTime asc";
//			}
//		}
		queryString = entityManager.createQuery(query).setFirstResult(paginationDataDto.getFrom())
				.setMaxResults(paginationDataDto.getTo());
		List<Object[]> greyhoundList = queryString.getResultList();
		for (Object[] object : greyhoundList) {
			ReportResponseDto reportResponseDto = new ReportResponseDto();
			reportResponseDto.setGreyhoundName((String) object[0]);
			reportResponseDto.setEntryNum((long) object[1]);
			reportResponseDto.setRating(object[2] != null ? Double.parseDouble(object[2].toString()) : 0);
			reportResponseDto.setStmhcp(object[3] != null ? Double.parseDouble(object[3].toString()) : 0);
			reportResponseDto.setWinTime(object[4] != null ? object[4].toString() : "0");
			reportResponseDto.setWeight(object[5] != null ? Double.parseDouble(object[5].toString()) : 0);
			reportResponseDto.setCalcTm(object[6] != null ? Double.parseDouble(object[6].toString()) : 0);
			reportResponseDto.setPrize(object[7] != null ? Double.parseDouble(object[7].toString()) : 0);
			reportResponseDto.setGreyhoundId((long) object[8]);
			dataList.add(reportResponseDto);
		}
		filterWithPagination.getPagination().setData(dataList);
		filterWithPagination.getPagination().setTotalCount(totalCounts);
		filterWithPagination.getPagination().setTotalPages(paginationDataDto.getTotalPages());
		return filterWithPagination.getPagination();
	}

	@Override
	public List<GreyhoundWithTrainerResponseDto> greyhoundByDateAndTrackAndTrainersRequestDto(
			GreyhoundByDateAndTrackAndTrainersRequestDto greyhoundByDateAndTrackAndTrainersRequestDto) {
		List<GreyhoundWithTrainerResponseDto> dataList = new ArrayList<>();
		String query = "select gd.id,gd.greyhoundId,gd.name,gd.trainerId,gd.stats,gd.track,gd.birthday "
				+ "from Greyhound gd join GreyhoundProfile gdp on gdp.greyhoundId = gd.greyhoundId where ";
		Calendar c = Calendar.getInstance();
		Date fromDate = greyhoundByDateAndTrackAndTrainersRequestDto.getFromDate();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);
		fromDate = c.getTime();

		Date toDate = greyhoundByDateAndTrackAndTrainersRequestDto.getToDate();
		c = Calendar.getInstance();
		c.setTime(toDate);
		c.add(Calendar.DATE, 1);
		toDate = c.getTime();
		query += "gdp.date between '" + Utility.getFormatedDateFromDate(fromDate) + " 00:00:00' and '"
				+ Utility.getFormatedDateFromDate(toDate) + " 00:00:00'";
		boolean flag = true;
		boolean whereFlag = false;
		if (greyhoundByDateAndTrackAndTrainersRequestDto.getTrack() != null
				&& !greyhoundByDateAndTrackAndTrainersRequestDto.getTrack().isEmpty()) {
			String tracksString = "(";
			for (String track : greyhoundByDateAndTrackAndTrainersRequestDto.getTrack()) {
				tracksString += "'" + track + "',";
			}
			tracksString = tracksString.substring(0, tracksString.length() - 1) + ")";
			query += Utility.addWhere(whereFlag);
			query += Utility.addANDOrOR(flag) + " gd.track in " + tracksString;
			flag = true;
			whereFlag = false;
		}
		if (greyhoundByDateAndTrackAndTrainersRequestDto.getTrainers() != null
				&& !greyhoundByDateAndTrackAndTrainersRequestDto.getTrainers().isEmpty()) {
			query += Utility.addWhere(whereFlag);
			String trainerIds = "(";
			for (Long trainer : greyhoundByDateAndTrackAndTrainersRequestDto.getTrainers()) {
				trainerIds += trainer + ",";
			}
			trainerIds = trainerIds.substring(0, trainerIds.length() - 1) + ")";
			query += Utility.addANDOrOR(flag) + " gd.trainerId in " + trainerIds;
			flag = true;
			whereFlag = false;
		}

		Query queryString = entityManager.createQuery(query);
		List<Object[]> greyhoundList = queryString.getResultList();
		for (Object[] object : greyhoundList) {
			Greyhound greyhound = new Greyhound();
			greyhound.setId((Long) object[0]);
			greyhound.setGreyhoundId((Long) object[1]);
			greyhound.setName((String) object[2]);
			greyhound.setTrainerId((Long) object[3]);
			greyhound.setStats((String) object[4]);
			greyhound.setTrack((String) object[5]);
			greyhound.setBirthday((String) object[6]);

			GreyhoundWithTrainerResponseDto greyhoundWithTrainerResponseDtoExist = dataList.stream()
					.filter(user -> greyhound.getGreyhoundId() == user.getGreyhoundId()).findFirst().orElse(null);
			if (greyhoundWithTrainerResponseDtoExist == null) {
				GreyhoundWithTrainerResponseDto greyhoundWithTrainerResponseDto = new GreyhoundWithTrainerResponseDto();
				greyhoundWithTrainerResponseDto.setGreyhoundId(greyhound.getGreyhoundId());
				greyhoundWithTrainerResponseDto.setGreyhoundName(greyhound.getName());
				Trainer trainer = trainerService.findById(greyhound.getTrainerId());
				greyhoundWithTrainerResponseDto.setTrainerId(trainer.getId());
				greyhoundWithTrainerResponseDto.setTrainerName(trainer.getName());
				greyhoundWithTrainerResponseDto.setStats(greyhound.getStats());
				greyhoundWithTrainerResponseDto.setTrack(greyhound.getTrack());
				dataList.add(greyhoundWithTrainerResponseDto);
			}
		}
		return dataList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getGradesByFilter(ReportRequestDto filter) {
		String query = "select distinct(r.raceClass) from GreyhoundProfile gdp "
				+ "join Greyhound gd on gdp.greyhoundId = gd.greyhoundId "
				+ "join Race r on r.raceId = gdp.raceId where ";

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
		if (filter.getTrack() != null && !filter.getTrack().isEmpty() && !filter.getTrack().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.track like '%" + filter.getTrack()
					+ "%'";
			flag = true;
			whereFlag = false;
		}

		if (filter.getTrainer() != null && filter.getTrainer() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.trainerId = " + filter.getTrainer();
			flag = true;
			whereFlag = false;
		}
		Query queryString = entityManager.createQuery(query);
		List<String> greyhoundList = queryString.getResultList();
		return greyhoundList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getDistancesByFilter(ReportRequestDto filter) {
		String query = "select distinct(r.distance) from GreyhoundProfile gdp "
				+ "join Greyhound gd on gdp.greyhoundId = gd.greyhoundId "
				+ "join Race r on r.raceId = gdp.raceId where ";

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
		if (filter.getTrack() != null && !filter.getTrack().isEmpty() && !filter.getTrack().equalsIgnoreCase("all")) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.track like '%" + filter.getTrack()
					+ "%'";
			flag = true;
			whereFlag = false;
		}

		if (filter.getTrainer() != null && filter.getTrainer() != 0) {
			query += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " gd.trainerId = " + filter.getTrainer();
			flag = true;
			whereFlag = false;
		}
		Query queryString = entityManager.createQuery(query);
		List<Integer> greyhoundList = queryString.getResultList();
		return greyhoundList;
	}
}
