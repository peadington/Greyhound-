package com.greyhound.service;

import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.GreyhoundGraphFilterDto;
import com.greyhound.dto.GreyhoundProfileDto;
import com.greyhound.dto.PaginationDto;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IGreyhoundProfileService {

	void getGreyhoundProfileByGreyhoundId(long greyhoundId, PaginationDto pagination, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getGreyhoundReportByFilter(GreyhoundGraphFilterDto greyhoundGraphFilterDto,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void updateComment(long id, String comment, ApiResponseDtoBuilder apiResponseDtoBuilder);

	boolean existsByGreyhoundIdAndMeetingIdAndRaceId(Long dogId, Long meetingID, Long raceId);

	void addGreyhoundProfileDetails(GreyhoundProfileDto greyhoundProfile, Long dogId, String racePrizes);

}
