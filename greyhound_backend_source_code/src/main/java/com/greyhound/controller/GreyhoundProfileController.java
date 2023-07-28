package com.greyhound.controller;

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
import com.greyhound.dto.GreyhoundGraphFilterDto;
import com.greyhound.dto.PaginationDto;
import com.greyhound.service.IGreyhoundProfileService;

/**
 * 
 * @author p4logics
 *
 */
@CrossOrigin(origins = "*", maxAge = 360000000)
@RestController
@RequestMapping(AppConstant.API_BASE_URL)
public class GreyhoundProfileController {

	@Autowired
	private IGreyhoundProfileService greyhoundProfileService;

	/**
	 * Get greyhound profiles
	 * 
	 * @param greyhoundId
	 * @return
	 */
	@RequestMapping(value = "/greyhound/getGreyhoundProfileByGreyhoundId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto getGreyhoundProfileByGreyhoundId(@RequestParam(required = true) long greyhoundId,
			@RequestBody PaginationDto pagination) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundProfileService.getGreyhoundProfileByGreyhoundId(greyhoundId, pagination, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Get greyhound reports
	 * 
	 * @param greyhoundIds
	 * @param chartKind
	 * @return
	 */
	@RequestMapping(value = "/greyhound/getGreyhoundReportByGreyhoundIdAndChartKind", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto getGreyhoundReportByGreyhoundIdAndChartKind(@RequestBody GreyhoundGraphFilterDto filter) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundProfileService.getGreyhoundReportByFilter(filter, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/greyhound/comment/update", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto updateComment(@RequestParam(required = true) long id,
			@RequestParam(required = true) String comment) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		greyhoundProfileService.updateComment(id, comment, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
}
