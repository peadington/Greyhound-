package com.greyhound.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greyhound.dto.RaceDto;
import com.greyhound.dto.RatingDto;
import com.greyhound.dto.TrapDto;
import com.greyhound.json.responseDto.GreyhoundProfileJsonDto;
import com.greyhound.json.responseDto.MeetingJsonDto;
import com.greyhound.json.responseDto.RaceJsonDto;
import com.greyhound.utility.DBUtility;
import com.greyhound.utility.Utility;

/**
 * 
 * @author akram
 *
 */
@Service
public class GreyhoundScraperServiceImpl {
	private static Logger logger = Logger.getLogger(GreyhoundScraperServiceImpl.class);

	public void startScrapper() {
		try {
			// DBUtility.createTables();
			ExecutorService executor = Executors.newFixedThreadPool(100);
			// LocalDate startDate = LocalDate.of(2021, 01, 01);
			LocalDate startDate = LocalDate.now().minusDays(1);
			scrapMeetingDetail(startDate, executor);
			executor.shutdown();
		} catch (Exception e) {
			logger.error("Exception in startScrapper :-" + e.getMessage());
		}

	}

	private void scrapMeetingDetail(LocalDate startDate, ExecutorService executor) {
		try {
			LocalDate sDate = startDate;
			logger.info("THREAD TASK ADDED  DATE :-" + sDate);
			executor.execute(new Thread() {
				@Override
				public void run() {
					try {
						logger.info("THREAD START  DATE :-" + sDate);
						Utility utility = new Utility();
						DBUtility dbutility = new DBUtility();
						String[] tracks = { "Newcastle", "Sunderland", "Pelaw Grange", "Crayford", "Romford",
								"Nottingham", "Perry Barr", "Sheffield" };
						for (String track : tracks) {
							String url = "https://api.gbgb.org.uk/api/results?page=1&itemsPerPage=900000000&track="
									+ track + "&date=" + sDate + "&race_type=race";
							Document document = utility.getDocument(url, 0);
							ObjectMapper mapper = new ObjectMapper();
							JsonNode items = mapper
									.readTree(document.html().split("<body>")[1].split("</body>")[0].trim())
									.get("items");
							if (items.size() > 0) {
								findDataFromJson(items, utility, dbutility, mapper);
								//addRating(sDate, track);
							} else {
								logger.info("Data not available for date:-" + sDate + " Track :-" + track);
							}
						}

					} catch (Exception e) {
						logger.error("Exception in scrapMeetingDetail THREAD :-" + e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			logger.error("Exception in scrapMeetingDetail :-" + e.getMessage());
		}

	}

	private void findDataFromJson(JsonNode items, Utility utility, DBUtility dbutility, ObjectMapper mapper) {
		try {
			CopyOnWriteArrayList<MeetingJsonDto> listOfMeeting = mapper.readValue(items.toPrettyString(),
					new TypeReference<CopyOnWriteArrayList<MeetingJsonDto>>() {
					});
			listOfMeeting.stream().forEach(meeting -> dbutility.insertIntoMeetingTable(meeting));
			getRaceAndTrapData(listOfMeeting.get(0), utility, dbutility, mapper);

		} catch (Exception e) {
			logger.error("Exception in findDataFromJson :-" + e.getMessage());
		}

	}

	public void getRaceAndTrapData(MeetingJsonDto meeting, Utility utility, DBUtility dbutility, ObjectMapper mapper) {
		try {
			String url = "https://api.gbgb.org.uk/api/results/meeting/" + meeting.getMeetingId();
			Document document = utility.getDocument(url, 0);
			JsonNode jsonNode = mapper.readTree(document.html().split("<body>")[1].split("</body>")[0].trim()).get(0);
			JsonNode items = jsonNode.get("races");
			if (items.size() > 0) {
				CopyOnWriteArrayList<RaceJsonDto> listOfRace = mapper.readValue(items.toPrettyString(),
						new TypeReference<CopyOnWriteArrayList<RaceJsonDto>>() {
						});
				for (RaceJsonDto race : listOfRace) {
					dbutility.insertIntoRaceTable(race, jsonNode);
//					CopyOnWriteArrayList<TrapDto> traps = race.getTraps();
//					for (TrapDto trap : traps) {
//						try {
//							if (trap.getTrainerName() != null) {
//								boolean isExist2 = DBUtility.checkIsExist(
//										trap.getTrainerName().replaceAll("'", " ").trim(), 1, "trainer", "name");
//								if (!isExist2 && !trap.getTrainerName().replaceAll("'", "").trim().isEmpty()) {
//									dbutility.insertIntoTrainerTable(trap);
//								}
//								dbutility.insertIntoTrapTable(trap, race, jsonNode, utility);
//								boolean isExist = DBUtility.checkIsExistForGreyhoundTable(trap.getDogId(),
//										jsonNode.get("trackName").asText(), "greyhound");
//								if (!isExist) {
//									dbutility.insertIntoGreyhoundTable(trap, jsonNode);
//								}
//								getGreyhoundProfileData(trap, meeting.getMeetingId(), race, dbutility, utility, mapper);
//							}
//						} catch (Exception e) {
//							logger.error("Exception in getRaceAndTrapData :-" + e.getMessage() + "  trap :=" + trap);
//							e.printStackTrace();
//						}
//					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getRaceAndTrapData :-" + e.getMessage() + "  meeting :=" + meeting);
			e.printStackTrace();
		}
	}

	private void getGreyhoundProfileData(TrapDto trap, Long meetingID, RaceDto race, DBUtility dbutility,
			Utility utility, ObjectMapper mapper) {
		try {
			String url = "https://api.gbgb.org.uk/api/results/dog/" + trap.getDogId()
					+ "?page=1&itemsPerPage=9000000000000&race_type=race";
			Document document = utility.getDocument(url, 0);
			JsonNode items = mapper.readTree(document.html().split("<body>")[1].split("</body>")[0].trim())
					.get("items");
			if (items.size() > 0) {
				CopyOnWriteArrayList<GreyhoundProfileJsonDto> listOfprofile = mapper.readValue(items.toPrettyString(),
						new TypeReference<CopyOnWriteArrayList<GreyhoundProfileJsonDto>>() {
						});

//				Optional<GreyhoundProfileJsonDto> greyhoundProfile = listOfprofile.stream()
//						.filter(g -> (g.getMeetingId().equals(meetingID)) && (g.getRaceId().equals(race.getRaceId())))
//						.findFirst();
//				boolean checkForProfileTable = dbutility.checkIsExistForGreyhoundProfileTable(trap.getDogId(),
//						meetingID, race.getRaceId(), "greyhound_profile");
//				if (!checkForProfileTable && greyhoundProfile.isPresent())
//					dbutility.insertIntoGreyhoundProfileTable(greyhoundProfile.get(), trap.getDogId(),
//							race.getRacePrizes());
			}
		} catch (Exception e) {
			logger.error("Exception in getGreyhoundProfileData :-" + e.getMessage());
		}
	}

//	private void addRating(LocalDate sDate, String track) {
//		Map<Long, List<RatingDto>> mapOfGreyhounds = new HashMap<>();
//		//ResultSet resultSet = DBUtility.getDistinctMeetingByDateAnTrack(sDate, track);
//		System.out.println("start process...");
//		try {
//			while (resultSet.next()) {
//				System.out.println("start process for meeting :-" + resultSet.getLong("meeting_id"));
//				ResultSet rsForRaces = DBUtility.getAllRacesByMeetingID(resultSet.getLong("meeting_id"));
//				while (rsForRaces.next()) {
//					try {
//						long meetingId = rsForRaces.getLong("meeting_id");
//						long distance = rsForRaces.getLong("distance");
//						long raceId = rsForRaces.getLong("race_id");
//						ResultSet rsForGreyhound = DBUtility.getAllGreyhoundByMeetingIDAndRaceID(meetingId, raceId);
//						while (rsForGreyhound.next()) {
//							try {
//								if (rsForGreyhound.getString("calc_tm") != null) {
//									if (mapOfGreyhounds.get(distance) != null) {
//										List<RatingDto> list = mapOfGreyhounds.get(distance);
//										RatingDto dto = new RatingDto();
//										dto.setMeetingId(meetingId);
//										dto.setRaceId(raceId);
//										dto.setGreyhoundId(rsForGreyhound.getLong("greyhound_id"));
//										dto.setCalculatedTime(Double.parseDouble(rsForGreyhound.getString("calc_tm")));
//										list.add(dto);
//									} else {
//										List<RatingDto> listOfGreyhounds = new ArrayList<>();
//										RatingDto dto = new RatingDto();
//										dto.setMeetingId(meetingId);
//										dto.setRaceId(raceId);
//										dto.setGreyhoundId(rsForGreyhound.getLong("greyhound_id"));
//										dto.setCalculatedTime(Double.parseDouble(rsForGreyhound.getString("calc_tm")));
//										listOfGreyhounds.add(dto);
//										mapOfGreyhounds.put(distance, listOfGreyhounds);
//									}
//								}
//							} catch (Exception e) {
//								logger.error("Exception in updateRating 1:-" + e.getMessage());
//							}
//						}
//
//					} catch (Exception e) {
//						logger.error("Exception in updateRating 2:-" + e.getMessage());
//					}
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		updateRating(mapOfGreyhounds);
//	}

	private void updateRating(Map<Long, List<RatingDto>> mapOfGreyhounds) {

		for (Entry<Long, List<RatingDto>> entry : mapOfGreyhounds.entrySet()) {
			try {
				List<RatingDto> listOfRatingDto = entry.getValue();
				listOfRatingDto.sort(Comparator.comparingDouble(RatingDto::getCalculatedTime));
				for (int i = 0; i < listOfRatingDto.size(); i++) {
					//DBUtility.updateRatingOfGreyhound(i + 1, listOfRatingDto.get(i));
				}
			} catch (Exception e) {
				logger.error("Exception in updateRating:-" + e.getMessage());
			}
		}

	}

}
