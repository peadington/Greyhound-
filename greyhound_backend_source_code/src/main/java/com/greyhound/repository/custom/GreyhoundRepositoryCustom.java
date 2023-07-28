package com.greyhound.repository.custom;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.greyhound.dto.GreyhoundByDateAndTrackAndTrainersRequestDto;
import com.greyhound.dto.GreyhoundCompareRequestDto;
import com.greyhound.dto.GreyhoundCompareResponseDto;
import com.greyhound.dto.GreyhoundFilterDto;
import com.greyhound.dto.GreyhoundFilterWithPagination;
import com.greyhound.dto.GreyhoundReportResponseDto;
import com.greyhound.dto.GreyhoundWithTrainerResponseDto;
import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.ReportFilterDtoWithPagination;
import com.greyhound.dto.ReportRequestDto;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface GreyhoundRepositoryCustom {

	PaginationDto getGreyhoundByFilterWithPagination(GreyhoundFilterWithPagination filterWithPagination);
	
	List<GreyhoundReportResponseDto> exportGreyhounds(GreyhoundFilterDto filter);

	List<GreyhoundCompareResponseDto> compareGreyhound(GreyhoundCompareRequestDto greyhoundCompare);

	Object exportReport(ReportRequestDto reportRequestDto);

	Object greyhoundGraphData(GreyhoundFilterDto filter);

	PaginationDto getGreyhoundDataByFilter(ReportFilterDtoWithPagination reportRequestDto);

	List<GreyhoundWithTrainerResponseDto> greyhoundByDateAndTrackAndTrainersRequestDto(
			GreyhoundByDateAndTrackAndTrainersRequestDto greyhoundByDateAndTrackAndTrainersRequestDto);

	List<String> getGradesByFilter(ReportRequestDto filter);

	List<Integer> getDistancesByFilter(ReportRequestDto filter);

}
