package com.greyhound.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.greyhound.constant.AppConstant;
import com.greyhound.dto.ApiResponseDto;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.ReportFilterDtoWithPagination;
import com.greyhound.dto.ReportRequestDto;
import com.greyhound.service.IReportService;

/**
 * 
 * @author p4logics
 *
 */
@CrossOrigin(origins = "*", maxAge = 360000000)
@RestController
@RequestMapping(AppConstant.API_BASE_URL)
public class ReportController {

	@Autowired
	private IReportService reportService;

	/**
	 * Forgot Password
	 * 
	 * @param email
	 * @param otp
	 * @return
	 */
	@RequestMapping(value = "/report/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto getReport(@RequestBody ReportFilterDtoWithPagination reportRequestDto) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		reportService.getReport(reportRequestDto, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/report/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto exportReport(@RequestBody ReportRequestDto reportRequestDto) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		reportService.exportReport(reportRequestDto, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
}
