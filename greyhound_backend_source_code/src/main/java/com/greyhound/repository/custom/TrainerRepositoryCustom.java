package com.greyhound.repository.custom;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.TrainerByDateAndTrackRequestDto;
import com.greyhound.dto.TrainerCompareRequestDto;
import com.greyhound.dto.TrainerFilterDto;
import com.greyhound.dto.TrainerFilterDtoWithPagination;
import com.greyhound.dto.TrainerResponseDto;
import com.greyhound.model.Trainer;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface TrainerRepositoryCustom {

	PaginationDto getTrainerByFilterWithPagination(TrainerFilterDtoWithPagination filterWithPagination);

	List<TrainerResponseDto> exportTrainers(TrainerFilterDto filter);

	Map<String, Map<String, String>> getTrainerGraphData(TrainerFilterDto filter);

	List<Trainer> getTrainersByTrack(String track);

	List<Trainer> getTrainersByTracks(List<String> tracks);

	Map<String, Map<String, String>> compareTrainer(TrainerCompareRequestDto trainerCompare);

	List<Trainer> getTrainersByDateAndTrack(TrainerByDateAndTrackRequestDto trainerByDateAndTrackRequestDto);

}
