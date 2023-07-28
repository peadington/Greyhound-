package com.greyhound.repository.custom.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.greyhound.dto.PaginationDataDto;
import com.greyhound.dto.PaginationDto;
import com.greyhound.repository.custom.GreyhoundProfileRepositoryCustom;
import com.greyhound.utility.Utility;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public class GreyhoundProfileRepositoryCustomImpl implements GreyhoundProfileRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public PaginationDto getGreyhoundProfileDetailsByGreyhoundId(long greyhoundId, PaginationDto pagination) {
		String query = "select gdp.byy,gdp.calcTm,gdp.date,gdp.position,gdp.sp,gdp.stmhcp,gdp.weight,gdp.winTime,r.distance,r.note,r.prizes,r.raceClass,r.raceTime,gdp.raceLink from GreyhoundProfile gdp join Race r on gdp.raceId = r.raceId join Meeting m on gdp.meetingId = m.meetingId where gdp.greyhoundId = "
				+ greyhoundId;
		query += " order by gdp.id desc";
		Query queryString = entityManager.createQuery(query);
		List<Object[]> dataList = queryString.getResultList();
		pagination.setTotalCount(dataList.size());
		PaginationDataDto paginationDataDto = Utility.getPaginationData(dataList.size(), pagination);
		queryString = entityManager.createQuery(query).setFirstResult(paginationDataDto.getFrom())
				.setMaxResults(paginationDataDto.getTo());
		dataList = queryString.getResultList();
		pagination.setData(dataList);
		pagination.setTotalPages(paginationDataDto.getTotalPages());
		return pagination;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getGreyhoundProfileDetailsByGreyhoundIdAndDate(Long greyhoundId, String date) {
		String query = "select gdp.calcTm,gdp.date,gdp.position,gdp.stmhcp,gdp.weight,gdp.winTime,r.distance from GreyhoundProfile gdp join Race r on gdp.raceId = r.raceId where gdp.greyhoundId = "
				+ greyhoundId + " and gdp.date = '" + date + "'";
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
		return dataList;
	}

}
