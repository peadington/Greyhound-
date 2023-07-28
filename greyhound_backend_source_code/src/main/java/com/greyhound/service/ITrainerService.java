package com.greyhound.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.TrainerByDateAndTrackRequestDto;
import com.greyhound.dto.TrainerCompareRequestDto;
import com.greyhound.dto.TrainerFilterDto;
import com.greyhound.dto.TrainerFilterDtoWithPagination;
import com.greyhound.dto.TrapDto;
import com.greyhound.model.Trainer;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface ITrainerService {

	void searchTrainers(TrainerFilterDtoWithPagination filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void exportTrainers(TrainerFilterDto filter, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getTrainerGraphData(TrainerFilterDto trainerFilter, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void searchTrainersByName(String keyword, ApiResponseDtoBuilder apiResponseDtoBuilder);

	List<Trainer> findAll();

	List<Trainer> findByNameContainingIgnoreCase(String trainer);

	Trainer findById(Long trainerId);

	void getTrainersByTrack(String track, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getTrainersByTracks(List<String> tracks, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void compareTrainer(TrainerCompareRequestDto trainerCompare, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void addTrainerDetails(TrapDto trap);

	Trainer findByName(String trainerName);

	boolean existsByName(String trim);

	void getTrainersByDateAndTrack(TrainerByDateAndTrackRequestDto trainerByDateAndTrackRequestDto,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

}
