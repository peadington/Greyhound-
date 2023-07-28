package com.greyhound.repository.custom;

import org.springframework.stereotype.Repository;

import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.UserFilterWithPaginationDto;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface UserRepositoryCustom {

	PaginationDto getUserListByFilterWithPagination(UserFilterWithPaginationDto filterWithPagination);

}
