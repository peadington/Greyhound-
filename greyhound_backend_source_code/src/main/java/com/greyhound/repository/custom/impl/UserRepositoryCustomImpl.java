package com.greyhound.repository.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.UserFilterWithPaginationDto;
import com.greyhound.mapper.CustomMapper;
import com.greyhound.repository.custom.UserRepositoryCustom;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
	
	@Autowired
	private CustomMapper customMapper;

	@Override
	public PaginationDto getUserListByFilterWithPagination(UserFilterWithPaginationDto filterWithPagination) {
		return filterWithPagination.getPagination();
	}

}
