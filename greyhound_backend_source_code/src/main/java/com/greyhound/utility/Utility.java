package com.greyhound.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greyhound.dto.PaginationDataDto;
import com.greyhound.dto.PaginationDto;
import com.greyhound.dto.RatingDto;
import com.greyhound.json.responseDto.GreyhoundProfileJsonDto;
import com.greyhound.json.responseDto.MeetingJsonDto;
import com.greyhound.json.responseDto.RaceJsonDto;
import com.greyhound.json.responseDto.TrapJsonDto;
import com.greyhound.model.User;
import com.greyhound.repository.UserRepository;

/**
 * 
 * @author p4logics
 *
 */
public class Utility {
	private static Logger logger = Logger.getLogger(Utility.class);

	public void findDataFromJson(JsonNode items, DBUtility dbutility, ObjectMapper mapper) {
		try {
			CopyOnWriteArrayList<MeetingJsonDto> listOfMeeting = mapper.readValue(items.toPrettyString(),
					new TypeReference<CopyOnWriteArrayList<MeetingJsonDto>>() {
					});
			listOfMeeting.stream().forEach(meeting -> {
				if (!dbutility.checkIsExistForMeetingTable(meeting.getMeetingId(), meeting.getRaceId(), "meeting")) {
					dbutility.insertIntoMeetingTable(meeting);
				}
			});
			List<Long> distinctMeetings = listOfMeeting.stream().map(MeetingJsonDto::getMeetingId).distinct()
					.collect(Collectors.toList());
			getRaceAndTrapData(distinctMeetings, dbutility, mapper);

		} catch (Exception e) {
			logger.error("Exception in findDataFromJson :-" + e.getMessage());
		}

	}

	public void getRaceAndTrapData(List<Long> distinctMeetings, DBUtility dbutility, ObjectMapper mapper) {

		for (Long meeting : distinctMeetings) {
			try {
				String url = "https://api.gbgb.org.uk/api/results/meeting/" + meeting;
				Document document = getDocument(url, 0);
				JsonNode jsonNode = mapper.readTree(document.html().split("<body>")[1].split("</body>")[0].trim())
						.get(0);
				JsonNode items = jsonNode.get("races");
				if (items.size() > 0) {
					CopyOnWriteArrayList<RaceJsonDto> listOfRace = mapper.readValue(items.toPrettyString(),
							new TypeReference<CopyOnWriteArrayList<RaceJsonDto>>() {
							});
					for (RaceJsonDto race : listOfRace) {
						if (!dbutility.checkIsExistForMeetingTable(meeting, race.getRaceId(), "race")) {
							dbutility.insertIntoRaceTable(race, jsonNode);
						}
						CopyOnWriteArrayList<TrapJsonDto> traps = race.getTraps();
						for (TrapJsonDto trap : traps) {
							try {
								if (trap.getTrainerName() != null) {
									boolean isExist2 = dbutility.checkIsExist(
											trap.getTrainerName().replaceAll("'", " ").trim(), 1, "trainer", "name");
									if (!isExist2 && !trap.getTrainerName().replaceAll("'", "").trim().isEmpty()) {
										dbutility.insertIntoTrainerTable(trap);
									}
//									if (!dbutility.checkIsExistForTrapTable(race.getRaceId(), trap.getDogId(),
//											"trap")) {
//										dbutility.insertIntoTrapTable(trap, race, jsonNode);
//									}
									int trainerIdByName = dbutility.getTrainerIdByName(trap.getTrainerName());
									boolean isExist = dbutility.checkIsExistForGreyhoundTable(trap.getDogId(),
											jsonNode.get("trackName").asText(), trainerIdByName, "greyhound");
									if (!isExist) {
										dbutility.insertIntoGreyhoundTable(trap, jsonNode, trainerIdByName);
									}
									getGreyhoundProfileData(trap, meeting, race, dbutility, mapper);
								}
							} catch (Exception e) {
								logger.error(
										"Exception in getRaceAndTrapData :-" + e.getMessage() + "  trap :=" + trap);
								e.printStackTrace();
							}
						}
					}
				} else {
					logger.info("Races are not available for meetingID :- " + distinctMeetings);
				}
			} catch (Exception e) {
				logger.error("Exception in getRaceAndTrapData :-" + e.getMessage() + "  meeting :=" + distinctMeetings);
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @param trap
	 * @param meetingID
	 * @param race
	 * @param dbutility
	 * @param mapper
	 */
	private void getGreyhoundProfileData(TrapJsonDto trap, Long meetingID, RaceJsonDto race, DBUtility dbutility,
			ObjectMapper mapper) {
		try {
			String url = "https://api.gbgb.org.uk/api/results/dog/" + trap.getDogId()
					+ "?page=1&itemsPerPage=9000000000000&race_type=race";
			Document document = getDocument(url, 0);
			JsonNode items = mapper.readTree(document.html().split("<body>")[1].split("</body>")[0].trim())
					.get("items");
			if (items.size() > 0) {
				CopyOnWriteArrayList<GreyhoundProfileJsonDto> listOfprofiles = mapper.readValue(items.toPrettyString(),
						new TypeReference<CopyOnWriteArrayList<GreyhoundProfileJsonDto>>() {
						});
				dbutility.insertIntoGreyhoundProfileTable(listOfprofiles, trap.getDogId(), race.getRacePrizes());
			}
		} catch (Exception e) {
			logger.error("Exception in getGreyhoundProfileData :-" + e.getMessage());
		}
	}

	/**
	 * Generate random password
	 * 
	 * @param len
	 * @return
	 */
	public static String generateRandomPassword(int len) {
		// A strong password has Cap_chars, Lower_chars,
		// numeric value and symbols. So we are using all of
		// them to generate our password
		String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String Small_chars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String symbols = "!@#$%^&*_=+-/.?<>)";
		String values = Capital_chars + Small_chars + numbers + symbols;
		// Using random method
		Random rndm_method = new Random();
		String password = "";
		for (int i = 0; i < len; i++) {
			char ch = values.charAt(rndm_method.nextInt(values.length()));
			password += ch;
		}
		return password;
	}

	/**
	 * Generate OTP
	 * 
	 * @return
	 */
	public static Integer generateOTP() {
		// create instance of Random class
		Random rand = new Random();
		// Generate random integers in range 999999
		return 100000 + rand.nextInt(900000);
	}

	/**
	 * Convert list of string to list of integer
	 * 
	 * @param listOfString
	 * @param function
	 * @return
	 */
	public static <T, U> List<U> convertListOfStringToListOfInteger(List<T> listOfString, Function<T, U> function) {
		return listOfString.stream().map(function).collect(Collectors.toList());
	}

	/**
	 * Add hour, minute and seconds in add
	 * 
	 * @param date
	 * @param hourOfDay
	 * @param minute
	 * @param seconds
	 * @return
	 */
	public static Date addHourMinAndSecToDate(Date date, int hourOfDay, int minute, int seconds) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.AM_PM, 0);
			calendar.set(Calendar.HOUR, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, seconds);
			return calendar.getTime();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getFormatedDateFromDate(Date date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = formatter.format(date);
			return strDate;

		} catch (Exception e) {
		}
		return null;
	}

	public static Date getDate(String date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return formatter.parse(date);

		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Check is number
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNumber(String val) {
		try {
			Integer.parseInt(val.trim());
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Get Authorized Username
	 * 
	 * @return
	 */
	public static User getSessionUser(UserRepository userRepository) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		User user = userRepository.findByEmail(currentUsername);
		return user;
	}

	public static char generateNextAlphabate(char ch) {
		int c = ch;
		c++;
		return (char) c;
	}

	public static PaginationDataDto getPaginationData(long totalCounts, PaginationDto paginationDto) {
		PaginationDataDto paginationDataDto = new PaginationDataDto();
		int totalPages = (int) Math.ceil((double) totalCounts / (double) paginationDto.getPerPage());
		int from = (paginationDto.getCurrentPage() - 1) * paginationDto.getPerPage();
		int to = paginationDto.getPerPage();
		paginationDataDto.setTotalPages(totalPages);
		paginationDataDto.setFrom(from);
		paginationDataDto.setTo(to);
		return paginationDataDto;
	}

	public static String addANDOrOR(boolean flag) {
		String condition = "";
		if (flag) {
			condition = " AND";
		}
		return condition;
	}

	public static String addWhere(boolean flag) {
		String condition = "";
		if (flag) {
			condition = " where";
		}
		return condition;
	}

	ObjectMapper mapper = new ObjectMapper();

	public Document getDocument(String url, int retry) {
		Document document = null;
		try {
			document = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).timeout(0).maxBodySize(0)
					.header("Accept", "application/json, text/plain, */*")
					.header("Accept-Language", "en-US,en-GB;q=0.9,en;q=0.8,es;q=0.7").header("Connection", "keep-alive")
					.header("Origin", "https://www.gbgb.org.uk")
					.header("User-Agent",
							"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
					.get();
			if (document == null && retry < 3) {
				retry++;
				document = getDocument(url, retry);
			}
		} catch (Exception e) {
			if (retry < 3) {
				retry++;
				document = getDocument(url, retry);
			}
		}
		return document;

	}

	public static String getByValue(String runTime, String winTime) {
		double raceWinTime = Double.parseDouble(runTime);
		double resultRunTime = Double.parseDouble(winTime);
		if (raceWinTime != 0 || resultRunTime != 0) {
			double e = (resultRunTime - raceWinTime) / 0.08;
			e = 0.25 * Math.round(e / 0.25);
			int t = (int) Math.floor(e);
			String n = decimalToFraction(e - t);
			if (t == 0) {
				t = 0;
			}
			if (t != 0 && n != "") {
				n = " " + n;
			}
			return t + n;
		}
		return "";
	}

	public static String decimalToFraction(double e) {
		if (e == 0) {
			return "";
		}
		int t = String.valueOf(e).length() - 2;
		double n = Math.pow(10, t);
		double i = e * n;
		double s = findGCD(i, n);
		n /= s;
		i /= s;
		return String.format("%.0f/%.0f", i, n);
	}

	public static double findGCD(double a, double b) {
		if (b == 0) {
			return a;
		}
		return findGCD(b, a % b);
	}

	public void addRating(LocalDate sDate, String track, DBUtility dbutility) {
		Map<Long, List<RatingDto>> mapOfGreyhounds = new HashMap<>();
		ResultSet resultSet = dbutility.getDistinctMeetingByDateAnTrack(sDate, track);
		logger.info("start rating process ...");
		try {
			while (resultSet.next()) {
				logger.info("start process for meeting :-" + resultSet.getLong("meeting_id"));
				ResultSet rsForRaces = dbutility.getAllRacesByMeetingID(resultSet.getLong("meeting_id"));
				while (rsForRaces.next()) {
					try {
						long meetingId = rsForRaces.getLong("meeting_id");
						long distance = rsForRaces.getLong("distance");
						long raceId = rsForRaces.getLong("race_id");
						ResultSet rsForGreyhound = dbutility.getAllGreyhoundByMeetingIDAndRaceID(meetingId, raceId);
						while (rsForGreyhound.next()) {
							try {
								if (rsForGreyhound.getString("calc_tm") != null) {
									if (mapOfGreyhounds.containsKey(distance)) {
										List<RatingDto> list = mapOfGreyhounds.get(distance);
										RatingDto dto = new RatingDto();
										dto.setMeetingId(meetingId);
										dto.setRaceId(raceId);
										dto.setGreyhoundId(rsForGreyhound.getLong("greyhound_id"));
										dto.setCalculatedTime(Double.parseDouble(rsForGreyhound.getString("calc_tm")));
										list.add(dto);
									} else {
										List<RatingDto> listOfGreyhounds = new ArrayList<>();
										RatingDto dto = new RatingDto();
										dto.setMeetingId(meetingId);
										dto.setRaceId(raceId);
										dto.setGreyhoundId(rsForGreyhound.getLong("greyhound_id"));
										dto.setCalculatedTime(Double.parseDouble(rsForGreyhound.getString("calc_tm")));
										listOfGreyhounds.add(dto);
										mapOfGreyhounds.put(distance, listOfGreyhounds);
									}
								}
							} catch (Exception e) {
								logger.error("Exception in updateRating 1:-" + e.getMessage());
							}
						}

					} catch (Exception e) {
						logger.error("Exception in updateRating 2:-" + e.getMessage());
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		updateRating(mapOfGreyhounds, dbutility);
	}

	private void updateRating(Map<Long, List<RatingDto>> mapOfGreyhounds, DBUtility dbutility) {

		for (Entry<Long, List<RatingDto>> entry : mapOfGreyhounds.entrySet()) {
			try {
				List<RatingDto> listOfRatingDto = entry.getValue();
				listOfRatingDto.sort(Comparator.comparingDouble(RatingDto::getCalculatedTime));
				for (int i = 0; i < listOfRatingDto.size(); i++) {
					dbutility.updateRatingOfGreyhound(i + 1, listOfRatingDto.get(i));
				}
			} catch (Exception e) {
				logger.error("Exception in updateRating:-" + e.getMessage());
			}
		}

	}
}
