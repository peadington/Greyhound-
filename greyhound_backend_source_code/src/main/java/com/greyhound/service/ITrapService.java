package com.greyhound.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.RaceDto;
import com.greyhound.dto.TrapDto;
import com.greyhound.model.Trap;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface ITrapService {

	void getAllTrap(ApiResponseDtoBuilder apiResponseDtoBuilder);

	List<Trap> findByGreyhoundId(long greyhoundId);

	void addTrapDetails(TrapDto trap, RaceDto race);

}
