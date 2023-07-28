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
import com.greyhound.dto.TrainerByDateAndTrackRequestDto;
import com.greyhound.dto.TrainerCompareRequestDto;
import com.greyhound.dto.TrainerFilterDto;
import com.greyhound.dto.TrainerFilterDtoWithPagination;
import com.greyhound.service.ITrainerService;

/**
 * 
 * @author p4logics
 *
 */
@CrossOrigin(origins = "*", maxAge = 360000000)
@RestController
@RequestMapping(AppConstant.API_BASE_URL)
public class TrainerController {

	@Autowired
	private ITrainerService trainerService;

	/**
	 * Get trainers
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/trainers/searchByName", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto searchTrainersByName(@RequestParam(required = false) String keyword) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		trainerService.searchTrainersByName(keyword, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Get trainers
	 * 
	 * @param track
	 * @return
	 */
	@RequestMapping(value = "/trainers/getByTrack", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto getTrainersByTrack(@RequestParam(required = false) String track) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		trainerService.getTrainersByTrack(track, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Get trainers
	 * 
	 * @param track
	 * @return
	 */
	@RequestMapping(value = "/trainers/getByDateAndTrack", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto getTrainersByDateAndTrack(
			@RequestBody TrainerByDateAndTrackRequestDto trainerByDateAndTrackRequestDto) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		trainerService.getTrainersByDateAndTrack(trainerByDateAndTrackRequestDto, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Get trainers
	 * 
	 * @param tracks
	 * @return
	 */
	@RequestMapping(value = "/trainers/getByTracks", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto getTrainersByTracks(@RequestParam(required = false) List<String> tracks) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		trainerService.getTrainersByTracks(tracks, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Search trainers
	 * 
	 * @param filterWithPagination
	 * @return
	 */
	@RequestMapping(value = "/trainers/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto searchTrainers(@RequestBody TrainerFilterDtoWithPagination filterWithPagination) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		trainerService.searchTrainers(filterWithPagination, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Export trainers
	 * 
	 * @param filter
	 * @return
	 */
	@RequestMapping(value = "/trainers/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto exportTrainers(@RequestBody TrainerFilterDto filter) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		trainerService.exportTrainers(filter, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Get trainers
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/trainers/get/graph", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto getTrainerGraphData(@RequestBody TrainerFilterDto trainerFilter) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		trainerService.getTrainerGraphData(trainerFilter, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/trainer/compare", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto compareTrainer(@RequestBody TrainerCompareRequestDto trainerCompare) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		trainerService.compareTrainer(trainerCompare, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
}
