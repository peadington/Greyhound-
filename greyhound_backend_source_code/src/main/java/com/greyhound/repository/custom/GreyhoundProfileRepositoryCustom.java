package com.greyhound.repository.custom;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.greyhound.dto.PaginationDto;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface GreyhoundProfileRepositoryCustom {

	PaginationDto getGreyhoundProfileDetailsByGreyhoundId(long greyhoundId, PaginationDto pagination);

	List<Object[]> getGreyhoundProfileDetailsByGreyhoundIdAndDate(Long greyhoundId, String format);

}
