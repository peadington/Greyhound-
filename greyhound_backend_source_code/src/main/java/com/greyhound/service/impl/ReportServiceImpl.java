package com.greyhound.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.ReportFilterDtoWithPagination;
import com.greyhound.dto.ReportRequestDto;
import com.greyhound.service.IGreyhoundService;
import com.greyhound.service.IReportService;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class ReportServiceImpl implements IReportService {

	@Autowired
	private IGreyhoundService greyhoundService;

	@Override
	public void getReport(ReportFilterDtoWithPagination filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		PaginationDto pagination = greyhoundService.getGreyhoundDataByFilter(filterWithPagination);
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound Data").withData(pagination);
	}

	@Override
	public void exportReport(ReportRequestDto reportRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound Data")
				.withData(greyhoundService.exportReport(reportRequestDto));
	}
}
