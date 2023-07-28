package com.greyhound.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.RaceDto;
import com.greyhound.dto.ReportRequestDto;
import com.greyhound.model.Race;
import com.greyhound.repository.RaceRepository;
import com.greyhound.repository.custom.GreyhoundRepositoryCustom;
import com.greyhound.service.IRaceService;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class RaceServiceImpl implements IRaceService {

	static Logger logger = Logger.getLogger(RaceServiceImpl.class);

	@Autowired
	private RaceRepository raceRepository;

	@Autowired
	private GreyhoundRepositoryCustom greyhoundRepositoryCustom;

	@Override
	public void getDistance(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Integer> dataList = raceRepository.findDistinctDistance();
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Distance List").withData(dataList);
	}

	@Override
	public void getGrade(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<String> dataList = raceRepository.findDistinctGrade();
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Grade List").withData(dataList);
	}

	@Override
	public List<Race> findByRaceId(Long raceId) {
		return raceRepository.findByRaceId(raceId);
	}

	@Override
	public void addRaceDetails(RaceDto raceDto, JsonNode jsonNode) {
		Race race = new Race();
		race.setDistance(raceDto.getRaceDistance());
		race.setMeetingId(jsonNode.get("meetingId").asLong());
		race.setNote(raceDto.getRaceTitle());
		race.setPrizes(raceDto.getRacePrizes());
		race.setRaceClass(raceDto.getRaceClass());
		race.setRaceId(raceDto.getRaceId());
		race.setRaceTime(raceDto.getRaceTime());
		raceRepository.save(race);
		logger.info("Data Insert Successfully in Race table meetingID:-" + jsonNode.get("meetingId").asLong());
	}

	@Override
	public void getDistancesByFilter(ReportRequestDto reportRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Distance List")
				.withData(greyhoundRepositoryCustom.getDistancesByFilter(reportRequestDto));
	}

	@Override
	public void getGradesByFilter(ReportRequestDto reportRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Grade List")
				.withData(greyhoundRepositoryCustom.getGradesByFilter(reportRequestDto));
	}

}
