package com.greyhound.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.GreyhoundDetailsResponseDtoForTrackAndName;
import com.greyhound.dto.GreyhoundGraphFilterDto;
import com.greyhound.dto.GreyhoundProfileDto;
import com.greyhound.dto.PaginationDto;
import com.greyhound.model.GreyhoundProfile;
import com.greyhound.repository.GreyhoundProfileRepository;
import com.greyhound.repository.custom.GreyhoundProfileRepositoryCustom;
import com.greyhound.service.IGreyhoundProfileService;
import com.greyhound.utility.Utility;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class GreyhoundProfileServiceImpl implements IGreyhoundProfileService {

	static Logger logger = Logger.getLogger(GreyhoundProfileServiceImpl.class);

	@Autowired
	private GreyhoundProfileRepositoryCustom greyhoundProfileRepositoryCustom;

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

	@Autowired
	private GreyhoundProfileRepository greyhoundProfileRepository;

	@Override
	public void getGreyhoundProfileByGreyhoundId(long greyhoundId, PaginationDto pagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<GreyhoundDetailsResponseDtoForTrackAndName> dataList = new ArrayList<>();
		pagination = greyhoundProfileRepositoryCustom.getGreyhoundProfileDetailsByGreyhoundId(greyhoundId, pagination);
		List<Object[]> greyhoundProfileList = (List<Object[]>) pagination.getData();
		for (Object[] greyhoundProfile : greyhoundProfileList) {
			GreyhoundDetailsResponseDtoForTrackAndName greyhoundDetailsResponseDtoForTrackAndName = new GreyhoundDetailsResponseDtoForTrackAndName();
			greyhoundDetailsResponseDtoForTrackAndName.setCalcTm((String) greyhoundProfile[1]);
			greyhoundDetailsResponseDtoForTrackAndName.setDate((String) greyhoundProfile[2]);
			greyhoundDetailsResponseDtoForTrackAndName.setPosition((int) greyhoundProfile[3]);
			greyhoundDetailsResponseDtoForTrackAndName.setsTmHcp((String) greyhoundProfile[5]);
			greyhoundDetailsResponseDtoForTrackAndName.setWeight((String) greyhoundProfile[6]);
			greyhoundDetailsResponseDtoForTrackAndName.setWinTime((String) greyhoundProfile[7]);
			greyhoundDetailsResponseDtoForTrackAndName.setDistance((int) greyhoundProfile[8]);
			greyhoundDetailsResponseDtoForTrackAndName.setRaceUrl((String) greyhoundProfile[9]);
			dataList.add(greyhoundDetailsResponseDtoForTrackAndName);
		}
		pagination.setData(dataList);
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Greyhound List").withData(pagination);
	}

	@Override
	public void getGreyhoundReportByFilter(GreyhoundGraphFilterDto greyhoundGraphFilterDto,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Map<String, Map<String, String>> dataMap = new HashMap<>();
		for (Long greyhoundId : greyhoundGraphFilterDto.getGreyhoundIds()) {
			Calendar start = Calendar.getInstance();
			start.setTime(greyhoundGraphFilterDto.getFromDate());
			Calendar end = Calendar.getInstance();
			end.setTime(greyhoundGraphFilterDto.getToDate());
			end.add(Calendar.DATE, 1);
			Map<String, String> map = new LinkedHashMap<>();
			for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
				List<Object[]> greyhoundProfileList = greyhoundProfileRepositoryCustom
						.getGreyhoundProfileDetailsByGreyhoundIdAndDate(greyhoundId, formatter.format(date));
				if (!greyhoundProfileList.isEmpty()) {
					for (Object[] greyhoundProfile : greyhoundProfileList) {
						if (greyhoundGraphFilterDto.getChartKind().equalsIgnoreCase("distance")) {
							map.put(formatter.format(date), greyhoundProfile[0].toString());
						} else if (greyhoundGraphFilterDto.getChartKind().equalsIgnoreCase("position")) {
							map.put(formatter.format(date), greyhoundProfile[3].toString());
						} else if (greyhoundGraphFilterDto.getChartKind().equalsIgnoreCase("weight")) {
							map.put(formatter.format(date), greyhoundProfile[6].toString());
						} else if (greyhoundGraphFilterDto.getChartKind().equalsIgnoreCase("stmhcp")) {
							map.put(formatter.format(date), greyhoundProfile[5].toString());
						} else if (greyhoundGraphFilterDto.getChartKind().equalsIgnoreCase("calctm")) {
							map.put(formatter.format(date), greyhoundProfile[1].toString());
						}
					}
				} else {
					map.put(formatter.format(date), "0");
				}
			}
			dataMap.put(greyhoundId.toString(), map);
		}
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Chart Data").withData(dataMap);
	}

	@Override
	public void updateComment(long id, String comment, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<GreyhoundProfile> greyhoundProfile = greyhoundProfileRepository.findById(id);
		greyhoundProfile.get().setRemarks(comment);
		greyhoundProfileRepository.save(greyhoundProfile.get());
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Updated");
	}

	@Override
	public boolean existsByGreyhoundIdAndMeetingIdAndRaceId(Long dogId, Long meetingID, Long raceId) {
		return greyhoundProfileRepository.existsByGreyhoundIdAndMeetingIdAndRaceId(dogId, meetingID, raceId);
	}

	@Override
	public void addGreyhoundProfileDetails(GreyhoundProfileDto greyhoundProfileDto, Long dogId, String racePrizes) {
		GreyhoundProfile greyhoundProfile = new GreyhoundProfile();
		if (greyhoundProfileDto.getRaceWinTime() != null && greyhoundProfileDto.getResultRunTime() != null) {
			greyhoundProfile.setByy(
					Utility.getByValue(greyhoundProfileDto.getRaceWinTime(), greyhoundProfileDto.getResultRunTime()));
		}
		greyhoundProfile.setCalcTm(greyhoundProfileDto.getResultAdjustedTime());
		try {
			greyhoundProfile.setDate(new java.sql.Date(formatter.parse(greyhoundProfileDto.getRaceDate()).getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		greyhoundProfile.setGreyhoundId(dogId);
		greyhoundProfile.setMeetingId(greyhoundProfileDto.getMeetingId());
		greyhoundProfile.setPosition(
				(int) (greyhoundProfileDto.getResultPosition() != null ? greyhoundProfileDto.getResultPosition() : 0));
		greyhoundProfile.setRaceId(greyhoundProfileDto.getRaceId());
		greyhoundProfile.setRaceLink("https://www.gbgb.org.uk/meeting/?meetingId=" + greyhoundProfileDto.getMeetingId()
				+ "&raceId=" + greyhoundProfileDto.getRaceId());
		greyhoundProfile.setRemarks(greyhoundProfileDto.getResultComment());
		greyhoundProfile.setSp(greyhoundProfileDto.getSP());
		greyhoundProfile.setStmhcp(greyhoundProfileDto.getResultSectionalTime());
		greyhoundProfile.setWeight(greyhoundProfileDto.getResultDogWeight());
		greyhoundProfile.setWinTime(greyhoundProfileDto.getRaceWinTime());
		String[] prizes = racePrizes.split("\\|");
		for (String priize : prizes) {
			if (String.valueOf(priize.trim().charAt(0)).trim()
					.equals(String.valueOf(greyhoundProfileDto.getResultPosition()).trim())) {
				greyhoundProfile.setPrize(Integer.parseInt(priize.trim().split(" ")[1].trim().replaceAll("£", "")));
				break;
			} else if (priize.contains("Others")) {
				greyhoundProfile.setPrize(Integer.parseInt(priize.trim().split(" ")[1].trim().replaceAll("£", "")));
				break;
			}
		}
		for (String priize : prizes) {
			if (String.valueOf(priize.trim().charAt(0)).trim()
					.equals(String.valueOf(greyhoundProfileDto.getResultPosition()).trim())) {
				greyhoundProfile.setPrize(Integer.parseInt(priize.trim().split(" ")[1].trim().replaceAll("£", "")));
				break;

			} else if (priize.contains("Others")) {
				greyhoundProfile.setPrize(Integer.parseInt(priize.trim().split(" ")[1].trim().replaceAll("£", "")));
				break;
			} else if (priize.contains("Total")) {
				greyhoundProfile.setPrize(0);
				break;
			} else {
				greyhoundProfile.setPrize(0);
				break;
			}

		}
		greyhoundProfileRepository.save(greyhoundProfile);
		logger.info("Data Insert Successfully in GreyHoundProfile table MeetingID:-" + greyhoundProfile.getMeetingId());
	}
}
