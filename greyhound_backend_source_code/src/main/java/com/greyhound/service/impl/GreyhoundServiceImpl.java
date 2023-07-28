package com.greyhound.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.GreyhoundByDateAndTrackAndTrainersRequestDto;
import com.greyhound.dto.GreyhoundCompareRequestDto;
import com.greyhound.dto.GreyhoundFilterDto;
import com.greyhound.dto.GreyhoundFilterWithPagination;
import com.greyhound.dto.GreyhoundResponseDtoForTrackAndName;
import com.greyhound.dto.GreyhoundWithTrainerResponseDto;
import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.ReportFilterDtoWithPagination;
import com.greyhound.dto.ReportRequestDto;
import com.greyhound.dto.TrapDto;
import com.greyhound.model.Greyhound;
import com.greyhound.model.Trainer;
import com.greyhound.repository.GreyhoundRepository;
import com.greyhound.repository.custom.GreyhoundRepositoryCustom;
import com.greyhound.service.IGreyhoundService;
import com.greyhound.service.ITrainerService;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class GreyhoundServiceImpl implements IGreyhoundService {

	static Logger logger = Logger.getLogger(GreyhoundServiceImpl.class);

	@Autowired
	private GreyhoundRepository greyhoundRepository;

	@Autowired
	private GreyhoundRepositoryCustom greyhoundRepositoryCustom;

	@Autowired
	private ITrainerService trainerService;

	@Override
	public void getAllGreyhound(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Greyhound> greyhoundList = greyhoundRepository.findAll();
		if (!greyhoundList.isEmpty())
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(greyhoundList);
		else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

	@Override
	public void searchGreyhoundsByName(String keyword, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Greyhound> greyhoundList = new ArrayList<>();
		if (keyword != null && !keyword.isEmpty())
			greyhoundList = greyhoundRepository.findByNameContainingIgnoreCase(keyword);
		if (!greyhoundList.isEmpty())
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(greyhoundList);
		else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

	@Override
	public void searchGreyhoundByTrainer(long trainer, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Greyhound> greyhoundList = greyhoundRepository.findByTrainerId(trainer);
		if (!greyhoundList.isEmpty()) {
			Map<Long, Greyhound> map = new LinkedHashMap<Long, Greyhound>();
			for (Greyhound greyhound : greyhoundList) {
				if (!map.containsKey(greyhound.getGreyhoundId())) {
					map.put(greyhound.getGreyhoundId(), greyhound);
				}
			}
			List<Greyhound> dataList = new ArrayList<>();
			for (Long greyhound : map.keySet()) {
				dataList.add(map.get(greyhound));
			}
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(dataList);
		} else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

	@Override
	public void getGreyhoundByTrackAndName(String track, String name, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<GreyhoundResponseDtoForTrackAndName> dataList = new ArrayList<>();
		List<Greyhound> greyhoundList = new ArrayList<>();
		if ((track == null || track.isEmpty()) && (name == null || name.isEmpty())) {
			greyhoundList = greyhoundRepository.findAll();
		} else if (track != null && !track.isEmpty() && (name == null || name.isEmpty())) {
			greyhoundList = greyhoundRepository.findByTrackContainingIgnoreCase(track);
		} else if (name != null && !name.isEmpty() && (track == null || track.isEmpty())) {
			greyhoundList = greyhoundRepository.findByNameContainingIgnoreCase(name);
		} else if (track != null && !track.isEmpty() && name != null && !name.isEmpty()) {
			greyhoundList = greyhoundRepository.findByTrackContainingIgnoreCaseAndNameContainingIgnoreCase(track, name);
		}
		for (Greyhound greyhound : greyhoundList) {
			GreyhoundResponseDtoForTrackAndName greyhoundResponseDtoForTrackAndName = new GreyhoundResponseDtoForTrackAndName();
			greyhoundResponseDtoForTrackAndName.setGreyhoundId(greyhound.getId());
			greyhoundResponseDtoForTrackAndName.setName(greyhound.getName());
			greyhoundResponseDtoForTrackAndName.setTrack(greyhound.getTrack());
			greyhoundResponseDtoForTrackAndName.setTrainer(greyhound.getTrainerId().toString());
			greyhoundResponseDtoForTrackAndName.setBirthday(greyhound.getBirthday());
			dataList.add(greyhoundResponseDtoForTrackAndName);
		}
		if (!dataList.isEmpty())
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(dataList);
		else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

	@Override
	public List<Greyhound> findAll() {
		return greyhoundRepository.findAll();
	}

	@Override
	public void searchGreyhounds(GreyhoundFilterWithPagination filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List")
				.withData(greyhoundRepositoryCustom.getGreyhoundByFilterWithPagination(filterWithPagination));
	}

	@Override
	public void exportGreyhounds(GreyhoundFilterDto filter, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List")
				.withData(greyhoundRepositoryCustom.exportGreyhounds(filter));
	}

	@Override
	public void greyhoundGraphData(GreyhoundFilterDto filter, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List")
				.withData(greyhoundRepositoryCustom.greyhoundGraphData(filter));
	}

	@Override
	public Greyhound findByGreyhoundId(Long greyhoundId) {
		return greyhoundRepository.findByGreyhoundId(greyhoundId);
	}

	@Override
	public void startScraper(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		try {
			Process p = Runtime.getRuntime().exec("python /root/greyhound-python/scrap.py");
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Scraper Started");
	}

	@Override
	public void compareGreyhound(GreyhoundCompareRequestDto greyhoundCompare,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List")
				.withData(greyhoundRepositoryCustom.compareGreyhound(greyhoundCompare));
	}

	@Override
	public Object exportReport(ReportRequestDto reportRequestDto) {
		return greyhoundRepositoryCustom.exportReport(reportRequestDto);
	}

	@Override
	public PaginationDto getGreyhoundDataByFilter(ReportFilterDtoWithPagination reportRequestDto) {
		return greyhoundRepositoryCustom.getGreyhoundDataByFilter(reportRequestDto);
	}

	@Override
	public void addGreyhoundDetails(TrapDto trapDto, JsonNode jsonNode) {
		Greyhound greyhound = new Greyhound();
		greyhound.setBirthday(trapDto.getDogBorn());
		greyhound.setGreyhoundId(trapDto.getDogId() == null ? 0 : trapDto.getDogId());
		greyhound.setName(trapDto.getDogName());
		greyhound.setStats(trapDto.getDogSire() + " - " + trapDto.getDogDam() + " | " + trapDto.getDogSex() + "-"
				+ trapDto.getDogColour() + " | " + trapDto.getDogBorn()
				+ ((trapDto.getDogSeason() == null) ? " | -" : " | " + trapDto.getDogSeason()));
		greyhound.setTrack(jsonNode.get("trackName").asText());
		Trainer trainer = trainerService.findByName(trapDto.getTrainerName());
		greyhound.setTrainerId(trainer != null ? trainer.getId() : 0);
		greyhoundRepository.save(greyhound);
		logger.info("Data Insert Successfully in Greyhound table MeetingID:-" + jsonNode.get("meetingId").asLong());
	}

	@Override
	public boolean existsByGreyhoundIdAndTrack(Long dogId, String track) {
		return greyhoundRepository.existsByGreyhoundIdAndTrack(dogId, track);
	}

	@Override
	public void searchGreyhoundByTrainers(List<Long> trainerList, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Greyhound> greyhoundList = greyhoundRepository.findByTrainerIdIn(trainerList);
		if (!greyhoundList.isEmpty()) {
			List<GreyhoundWithTrainerResponseDto> dataList = new ArrayList<>();
			for (Greyhound greyhound : greyhoundList) {
				GreyhoundWithTrainerResponseDto greyhoundWithTrainerResponseDto = new GreyhoundWithTrainerResponseDto();
				greyhoundWithTrainerResponseDto.setGreyhoundId(greyhound.getGreyhoundId());
				greyhoundWithTrainerResponseDto.setGreyhoundName(greyhound.getName());
				Trainer trainer = trainerService.findById(greyhound.getTrainerId());
				greyhoundWithTrainerResponseDto.setTrainerId(trainer.getId());
				greyhoundWithTrainerResponseDto.setTrainerName(trainer.getName());
				dataList.add(greyhoundWithTrainerResponseDto);
			}
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(dataList);
		} else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

	@Override
	public void getGreyhoundsByTracksAndTrainer(long trainer, List<String> tracks,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Greyhound> greyhoundList = greyhoundRepository.findByTrackInAndTrainerId(tracks, trainer);
		if (!greyhoundList.isEmpty()) {
			Map<Long, Greyhound> map = new LinkedHashMap<Long, Greyhound>();
			for (Greyhound greyhound : greyhoundList) {
				if (!map.containsKey(greyhound.getGreyhoundId())) {
					map.put(greyhound.getGreyhoundId(), greyhound);
				}
			}
			List<Greyhound> dataList = new ArrayList<>();
			for (Long greyhound : map.keySet()) {
				dataList.add(map.get(greyhound));
			}
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(dataList);
		} else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

	@Override
	public void searchGreyhoundByTrackAndTrainers(String track, List<Long> trainerList,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Greyhound> greyhoundList = greyhoundRepository.findByTrackAndTrainerIdIn(track, trainerList);
		if (!greyhoundList.isEmpty()) {
			List<GreyhoundWithTrainerResponseDto> dataList = new ArrayList<>();
			for (Greyhound greyhound : greyhoundList) {
				GreyhoundWithTrainerResponseDto greyhoundWithTrainerResponseDto = new GreyhoundWithTrainerResponseDto();
				greyhoundWithTrainerResponseDto.setGreyhoundId(greyhound.getGreyhoundId());
				greyhoundWithTrainerResponseDto.setGreyhoundName(greyhound.getName());
				Trainer trainer = trainerService.findById(greyhound.getTrainerId());
				greyhoundWithTrainerResponseDto.setTrainerId(trainer.getId());
				greyhoundWithTrainerResponseDto.setTrainerName(trainer.getName());
				dataList.add(greyhoundWithTrainerResponseDto);
			}
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(dataList);
		} else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

	@Override
	public void searchByDateAndTrackAndTrainers(
			GreyhoundByDateAndTrackAndTrainersRequestDto greyhoundByDateAndTrackAndTrainersRequestDto,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<GreyhoundWithTrainerResponseDto> greyhoundList = greyhoundRepositoryCustom
				.greyhoundByDateAndTrackAndTrainersRequestDto(greyhoundByDateAndTrackAndTrainersRequestDto);
		if (!greyhoundList.isEmpty())
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(greyhoundList);
		else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

	@Override
	public void getByGreyhoundId(long id, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Greyhound greyhound = findByGreyhoundId(id);
		if (greyhound != null) {
			GreyhoundWithTrainerResponseDto greyhoundWithTrainerResponseDto = new GreyhoundWithTrainerResponseDto();
			greyhoundWithTrainerResponseDto.setGreyhoundId(greyhound.getGreyhoundId());
			greyhoundWithTrainerResponseDto.setGreyhoundName(greyhound.getName());
			Trainer trainer = trainerService.findById(greyhound.getTrainerId());
			greyhoundWithTrainerResponseDto.setTrainerId(trainer.getId());
			greyhoundWithTrainerResponseDto.setTrainerName(trainer.getName());
			greyhoundWithTrainerResponseDto.setStats(greyhound.getStats());
			greyhoundWithTrainerResponseDto.setTrack(greyhound.getTrack());
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound Info")
					.withData(greyhoundWithTrainerResponseDto);
		} else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Greyhound not found");
	}

}
