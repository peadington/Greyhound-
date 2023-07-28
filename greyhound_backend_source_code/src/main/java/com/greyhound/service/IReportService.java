package com.greyhound.service;

import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.ReportFilterDtoWithPagination;
import com.greyhound.dto.ReportRequestDto;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IReportService {

	void getReport(ReportFilterDtoWithPagination reportRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void exportReport(ReportRequestDto reportRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
