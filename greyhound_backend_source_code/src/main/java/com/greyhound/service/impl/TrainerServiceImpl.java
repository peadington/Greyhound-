package com.greyhound.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.TrainerByDateAndTrackRequestDto;
import com.greyhound.dto.TrainerCompareRequestDto;
import com.greyhound.dto.TrainerFilterDto;
import com.greyhound.dto.TrainerFilterDtoWithPagination;
import com.greyhound.dto.TrapDto;
import com.greyhound.model.Trainer;
import com.greyhound.repository.TrainerRepository;
import com.greyhound.repository.custom.TrainerRepositoryCustom;
import com.greyhound.service.ITrainerService;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class TrainerServiceImpl implements ITrainerService {

	static Logger logger = Logger.getLogger(TrainerServiceImpl.class);

	@Autowired
	private TrainerRepository trainerRepository;

	@Autowired
	private TrainerRepositoryCustom trainerRepositoryCustom;

	@Override
	public void searchTrainersByName(String keyword, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Trainer> trainerList = new ArrayList<>();
		if (keyword != null && !keyword.isEmpty())
			trainerList = findByNameContainingIgnoreCase(keyword);
		if (!trainerList.isEmpty())
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trainer List").withData(trainerList);
		else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Trainers not found");
	}

	@Override
	public void searchTrainers(TrainerFilterDtoWithPagination filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trainer Details")
				.withData(trainerRepositoryCustom.getTrainerByFilterWithPagination(filterWithPagination));
	}

	@Override
	public void exportTrainers(TrainerFilterDto filter, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trainer Details")
				.withData(trainerRepositoryCustom.exportTrainers(filter));
	}

	@Override
	public List<Trainer> findAll() {
		return trainerRepository.findAll();
	}

	@Override
	public List<Trainer> findByNameContainingIgnoreCase(String keyword) {
		return trainerRepository.findByNameContainingIgnoreCase(keyword);
	}

	@Override
	public Trainer findById(Long trainerId) {
		Optional<Trainer> trainer = trainerRepository.findById(trainerId);
		return trainer.isPresent() ? trainer.get() : null;
	}

	@Override
	public void getTrainerGraphData(TrainerFilterDto filter, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trainer Details")
				.withData(trainerRepositoryCustom.getTrainerGraphData(filter));
	}

	@Override
	public void getTrainersByTrack(String track, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trainer Details")
				.withData(trainerRepositoryCustom.getTrainersByTrack(track));
	}

	@Override
	public void getTrainersByTracks(List<String> tracks, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trainer Details")
				.withData(trainerRepositoryCustom.getTrainersByTracks(tracks));
	}

	@Override
	public void compareTrainer(TrainerCompareRequestDto trainerCompare, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trainer Details")
				.withData(trainerRepositoryCustom.compareTrainer(trainerCompare));
	}

	@Override
	public void addTrainerDetails(TrapDto trap) {
		Trainer trainer = new Trainer();
		trainer.setName(trap.getTrainerName().replaceAll("'", " ").trim());
		trainerRepository.save(trainer);
		logger.info("Data Insert Successfully in Trainer table trapNumber:-" + trap.getTrapNumber());
	}

	@Override
	public Trainer findByName(String trainerName) {
		return trainerRepository.findByName(trainerName);
	}

	@Override
	public boolean existsByName(String name) {
		return trainerRepository.existsByName(name);
	}

	@Override
	public void getTrainersByDateAndTrack(TrainerByDateAndTrackRequestDto trainerByDateAndTrackRequestDto,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Trainer Details")
				.withData(trainerRepositoryCustom.getTrainersByDateAndTrack(trainerByDateAndTrackRequestDto));
	}
}
