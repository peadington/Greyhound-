package com.greyhound.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greyhound.constant.AppConstant;
import com.greyhound.dto.ApiResponseDto;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.GreyhoundByDateAndTrackAndTrainersRequestDto;
import com.greyhound.dto.GreyhoundCompareRequestDto;
import com.greyhound.dto.GreyhoundFilterDto;
import com.greyhound.dto.GreyhoundFilterWithPagination;
import com.greyhound.service.IGreyhoundService;

/**
 * 
 * @author p4logics
 *
 */
@CrossOrigin(origins = "*", maxAge = 360000000)
@RestController
@RequestMapping(AppConstant.API_BASE_URL)
public class GreyhoundController {

	@Autowired
	private IGreyhoundService greyhoundService;

	/**
	 * Get greyhounds by name
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/greyhounds/searchByName", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto searchGreyhoundsByName(@RequestParam(required = false) String keyword) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.searchGreyhoundsByName(keyword, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/greyhound/get", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto getGreyhoundById(@RequestParam(required = true) long id) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.getByGreyhoundId(id, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Get greyhounds by trainer
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/greyhound/searchByTrainer", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto searchGreyhoundByTrainer(@RequestParam(required = false) long keyword) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.searchGreyhoundByTrainer(keyword, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/greyhound/searchByTrainers", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto searchGreyhoundByTrainers(@RequestParam(required = true) List<Long> trainer) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.searchGreyhoundByTrainers(trainer, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/greyhound/searchByTrackAndTrainers", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto searchGreyhoundByTrackAndTrainers(@RequestParam(required = true) String track,
			@RequestParam(required = true) List<Long> trainer) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.searchGreyhoundByTrackAndTrainers(track, trainer, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/greyhound/searchByDateAndTrackAndTrainers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto searchByDateAndTrackAndTrainers(
			@RequestBody GreyhoundByDateAndTrackAndTrainersRequestDto greyhoundByDateAndTrackAndTrainersRequestDto) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.searchByDateAndTrackAndTrainers(greyhoundByDateAndTrackAndTrainersRequestDto,
				apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/greyhound/getByTracksAndTrainer", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto getGreyhoundsByTracksAndTrainer(@RequestParam(required = true) List<String> tracks,
			@RequestParam(required = true) long trainer) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.getGreyhoundsByTracksAndTrainer(trainer, tracks, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Get greyhounds
	 * 
	 * @param filterWithPagination
	 * @return
	 */
	@RequestMapping(value = "/greyhound/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto searchGreyhounds(@RequestBody GreyhoundFilterWithPagination filterWithPagination) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.searchGreyhounds(filterWithPagination, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/greyhound/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto exportGreyhounds(@RequestBody GreyhoundFilterDto filter) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.exportGreyhounds(filter, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Get graph
	 * 
	 * @param filter
	 * @return
	 */
	@RequestMapping(value = "/greyhound/graph", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto greyhoundGraphData(@RequestBody GreyhoundFilterDto filter) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.greyhoundGraphData(filter, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Compare greyhounds
	 * 
	 * @param greyhoundCompare
	 * @return
	 */
	@RequestMapping(value = "/greyhound/compare", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto compareGreyhound(@RequestBody GreyhoundCompareRequestDto greyhoundCompare) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundService.compareGreyhound(greyhoundCompare, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
}
