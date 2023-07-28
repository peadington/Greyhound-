package com.greyhound.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.RaceDto;
import com.greyhound.dto.TrapDto;
import com.greyhound.model.Trap;
import com.greyhound.repository.TrapRepository;
import com.greyhound.service.ITrapService;
import com.greyhound.utility.Utility;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class TrapServiceImpl implements ITrapService {
	
	static Logger logger = Logger.getLogger(TrapServiceImpl.class);

	@Autowired
	private TrapRepository trapRepository;

	@Override
	public void getAllTrap(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Trap> trapList = trapRepository.findAll();
		if (!trapList.isEmpty())
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trap List").withData(trapList);
		else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Trap not found");
	}

	@Override
	public List<Trap> findByGreyhoundId(long greyhoundId) {
		return trapRepository.findByGreyhoundId(greyhoundId);
	}

	@Override
	public void addTrapDetails(TrapDto trapDto, RaceDto raceDto) {
		Trap trap = new Trap();
		trap.setComments(trapDto.getResultComment());
		trap.setGreyhoundId(trapDto.getDogId() == null ? 0 : trapDto.getDogId());
		trap.setNote(trapDto.getDogBorn() + " | " + trapDto.getResultDogWeight() + " | " + trapDto.getDogSex() + "-"
				+ trapDto.getDogColour() + " | " + trapDto.getDogSire() + " | " + trapDto.getDogDam());
		trap.setRaceId(raceDto.getRaceId());
		trap.setRankNo((int) (trapDto.getResultPosition() == null ? 0 : trapDto.getResultPosition()));
		trap.setSp(trapDto.getSP());
		if (trapDto.getResultRunTime() != null && trapDto.getResultAdjustedTime() != null) {
			trap.setTimeD(trapDto.getResultRunTime() + "("
					+ Utility.getByValue(trapDto.getResultAdjustedTime(), trapDto.getResultRunTime()) + ")");
		}
		trap.setTimeS(trapDto.getResultSectionalTime());
		trapRepository.save(trap);
		logger.info("Data Insert Successfully in Trap table RaceID:-" + raceDto.getRaceId());
	}

}
