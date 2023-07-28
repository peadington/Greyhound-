package com.greyhound.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.GreyhoundByDateAndTrackAndTrainersRequestDto;
import com.greyhound.dto.GreyhoundCompareRequestDto;
import com.greyhound.dto.GreyhoundFilterDto;
import com.greyhound.dto.GreyhoundFilterWithPagination;
import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.ReportFilterDtoWithPagination;
import com.greyhound.dto.ReportRequestDto;
import com.greyhound.dto.TrapDto;
import com.greyhound.model.Greyhound;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IGreyhoundService {

	void getAllGreyhound(ApiResponseDtoBuilder apiResponseDtoBuilder);

	void searchGreyhoundsByName(String keyword, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void searchGreyhoundByTrainer(long keyword, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getGreyhoundByTrackAndName(String track, String name, ApiResponseDtoBuilder apiResponseDtoBuilder);

	List<Greyhound> findAll();

	void searchGreyhounds(GreyhoundFilterWithPagination filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void exportGreyhounds(GreyhoundFilterDto filter, ApiResponseDtoBuilder apiResponseDtoBuilder);

	Greyhound findByGreyhoundId(Long greyhoundId);

	void startScraper(ApiResponseDtoBuilder apiResponseDtoBuilder);

	void compareGreyhound(GreyhoundCompareRequestDto greyhoundCompare, ApiResponseDtoBuilder apiResponseDtoBuilder);

	Object exportReport(ReportRequestDto reportRequestDto);

	void greyhoundGraphData(GreyhoundFilterDto filter, ApiResponseDtoBuilder apiResponseDtoBuilder);

	PaginationDto getGreyhoundDataByFilter(ReportFilterDtoWithPagination filterWithPagination);

	void addGreyhoundDetails(TrapDto trap, JsonNode jsonNode);

	boolean existsByGreyhoundIdAndTrack(Long dogId, String track);

	void searchGreyhoundByTrainers(List<Long> trainer, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getGreyhoundsByTracksAndTrainer(long trainer, List<String> tracks,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void searchGreyhoundByTrackAndTrainers(String track, List<Long> trainer,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void searchByDateAndTrackAndTrainers(
			GreyhoundByDateAndTrackAndTrainersRequestDto greyhoundByDateAndTrackAndTrainersRequestDto,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getByGreyhoundId(long id, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
