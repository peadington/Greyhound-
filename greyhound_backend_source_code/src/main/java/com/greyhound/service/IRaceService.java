package com.greyhound.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.RaceDto;
import com.greyhound.dto.ReportRequestDto;
import com.greyhound.model.Race;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IRaceService {

	void getDistance(ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getGrade(ApiResponseDtoBuilder apiResponseDtoBuilder);

	List<Race> findByRaceId(Long raceId);

	void addRaceDetails(RaceDto race, JsonNode jsonNode);

	void getDistancesByFilter(ReportRequestDto reportRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getGradesByFilter(ReportRequestDto reportRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
