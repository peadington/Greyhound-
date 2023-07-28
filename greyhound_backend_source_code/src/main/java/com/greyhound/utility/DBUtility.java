package com.greyhound.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.greyhound.dto.RatingDto;
import com.greyhound.json.responseDto.GreyhoundProfileJsonDto;
import com.greyhound.json.responseDto.MeetingJsonDto;
import com.greyhound.json.responseDto.RaceJsonDto;
import com.greyhound.json.responseDto.TrapJsonDto;

public class DBUtility {

	static Logger logger = Logger.getLogger(DBUtility.class);
	private Connection connection = null;
	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	public Connection getConnection() {
		String database = "greyhound_new_db";
		if (connection != null)
			return connection;
		else {
			String dbUrl = "jdbc:mysql://localhost:3306/" + database
					+ "?createDatabaseIfNotExist=true&connectTimeout=60000&socketTimeout=60000&autoReconnect=true&useSSL=false";
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				// connection = DriverManager.getConnection(dbUrl, "root", "root");
				connection = DriverManager.getConnection(dbUrl, "greyhound", "greyhound!@23");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return connection;
		}
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void createMeetingTable() {
		try {
			Connection connection = getConnection();
			java.sql.Statement stmt = connection.createStatement();
			String sql = "CREATE TABLE if not exists meeting" + "(id BIGINT not NULL AUTO_INCREMENT,"
					+ " date datetime, " + " meeting_id BIGINT, " + " note VARCHAR(255), " + " race_id BIGINT, "
					+ " track VARCHAR(255), " + " updatedAt datetime, " + " createdAt datetime, "
					+ " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);
			stmt.close();
			logger.info("Meeting table created successfully");

		} catch (Exception e) {
			logger.error("Exception in createMeetingTable :-" + e.getMessage());
		}
	}

	public void createTrainerTable() {
		try {
			Connection connection = getConnection();
			java.sql.Statement stmt = connection.createStatement();
			String sql = "CREATE TABLE if not exists trainer" + "(id BIGINT not NULL AUTO_INCREMENT,"
					+ " name varchar(255) ," + " updatedAt datetime, " + " createdAt datetime, "
					+ " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);
			stmt.close();
			logger.info("Trainer table created successfully");
		} catch (Exception e) {

			logger.error("Exception in createTrainerTable :-" + e.getMessage());
		}
	}

	public void createRaceTable() {
		try {
			Connection connection = getConnection();
			java.sql.Statement stmt = connection.createStatement();
			String sql = "CREATE TABLE if not exists race" + "(id BIGINT not NULL AUTO_INCREMENT,"
					+ " distance BIGINT, " + " meeting_id BIGINT, " + " note TEXT, " + " prizes TEXT, "
					+ " race_class VARCHAR(255), " + " race_id BIGINT, " + " race_time VARCHAR(255), "
					+ " updatedAt datetime, " + " createdAt datetime, " + " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);
			stmt.close();
			logger.info("Race table created successfully");
		} catch (Exception e) {
			logger.error("Exception in createRaceTable :-" + e.getMessage());
		}
	}

	public void insertIntoRaceTable(RaceJsonDto race, JsonNode jsonNode) {
		try {
			String query = "insert into race (distance,meeting_id,note,prizes,race_class,race_id,race_time) values(?,?,?,?,?,?,?)";
			PreparedStatement preparedStmt = getConnection().prepareStatement(query);
			preparedStmt.setDouble(1, race.getRaceDistance());
			preparedStmt.setLong(2, jsonNode.get("meetingId").asLong());
			preparedStmt.setString(3, race.getRaceTitle());
			preparedStmt.setString(4, race.getRacePrizes());
			preparedStmt.setString(5, race.getRacePrizes());
			preparedStmt.setString(5, race.getRaceClass());
			preparedStmt.setLong(6, race.getRaceId());
			preparedStmt.setString(7, race.getRaceTime());
			preparedStmt.execute();
			preparedStmt.close();
			logger.info("Data Insert Successfully in Race table meetingID:-" + jsonNode.get("meetingId").asLong());

		} catch (Exception e) {
			logger.error("Exception in insertIntoRaceTable :-" + e.getMessage());
		}
	}

	public void insertIntoTrainerTable(TrapJsonDto trap) {
		try {
			String query = "insert into trainer (name) values(?)";
			PreparedStatement preparedStmt = getConnection().prepareStatement(query);
			preparedStmt.setString(1, trap.getTrainerName().replaceAll("'", " ").trim());
			preparedStmt.execute();
			preparedStmt.close();
			logger.info("Data Insert Successfully in Trainer table trapNumber:-" + trap.getTrapNumber());
		} catch (Exception e) {
			logger.error("Exception in insertIntoTrainerTable :-" + e.getMessage());
		}
	}

	public synchronized boolean checkIsExist(String combination, int counts, String table_name, String column) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			String query = "SELECT COUNT(*) FROM " + table_name + " WHERE " + column + " = '" + combination + "'";
			statement = getConnection().createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception in checkIsExist :- " + e.getMessage());
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		return false;

	}

	public synchronized boolean checkIsExistForGreyhoundTable(Long greyhound_id, String track, int trainerIdByName,
			String table_name) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			String query = "SELECT COUNT(*) FROM " + table_name + " WHERE " + "greyhound_id = " + greyhound_id + " AND "
					+ " track =" + "'" + track + "'" + " AND " + " trainer_id =" + trainerIdByName;
			statement = getConnection().createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception in checkIsExistForGreyhoundTable :- " + e.getMessage());
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	public synchronized boolean checkIsExistForGreyhoundProfileTable(Long greyhound_id, Long meetingID, Long raceID,
			String table_name) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			String query = "SELECT COUNT(*) FROM " + table_name + " WHERE " + "greyhound_id" + " = '" + greyhound_id
					+ "' AND " + " meeting_id =" + "'" + meetingID + "'" + " AND " + " race_id =" + "'" + raceID + "'";
			statement = getConnection().createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception in checkIsExist :- " + e.getMessage());
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	public synchronized boolean checkIsExistForMeetingTable(Long meetingID, Long raceID, String table_name) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			String query = "SELECT COUNT(*) FROM " + table_name + " WHERE   meeting_id =" + "'" + meetingID + "'"
					+ " AND " + " race_id =" + "'" + raceID + "'";
			statement = getConnection().createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception in checkIsExist :- " + e.getMessage());
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	public synchronized boolean checkIsExistForTrapTable(Long raceID, Long greyhoundID, String table_name) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			String query = "SELECT COUNT(*) FROM " + table_name + " WHERE greyhound_id =" + "'" + greyhoundID + "'"
					+ " AND " + " race_id =" + "'" + raceID + "'";
			statement = getConnection().createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception in checkIsExist :- " + e.getMessage());
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	public void insertIntoMeetingTable(MeetingJsonDto meeting) {
		try {
			String query = "insert into meeting (date,meeting_id,note,race_id,track) values(?,?,?,?,?)";
			PreparedStatement preparedStmt = getConnection().prepareStatement(query);
			preparedStmt.setDate(1, new java.sql.Date(format.parse(meeting.getRaceDate()).getTime()));
			preparedStmt.setLong(2, meeting.getMeetingId());
			preparedStmt.setString(3, null);
			preparedStmt.setLong(4, meeting.getRaceId());
			preparedStmt.setString(5, meeting.getTrackName());
			preparedStmt.execute();
			preparedStmt.close();
			logger.info("Data Insert Successfully in meeting table meetingID:-" + meeting.getMeetingId());

		} catch (Exception e) {
			logger.error("Exception in insertIntoMeetingTable :-" + e.getMessage());
		}
	}

	public void createTrapTable() {
		try {
			Connection connection = getConnection();
			java.sql.Statement stmt = connection.createStatement();
			String sql = "CREATE TABLE if not exists trap" + "(id BIGINT not NULL AUTO_INCREMENT," + " comments TEXT, "
					+ " greyhound_id BIGINT, " + " note Text, " + " race_id BIGINT, " + " rank_no BIGINT, "
					+ " sp varchar(255), " + " timed VARCHAR(255), " + " times VARCHAR(255), " + " prize BIGINT, "
					+ " updatedAt datetime, " + " createdAt datetime, " + " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);
			logger.info(" trap table created successfully");
		} catch (Exception e) {

			logger.error("Exception in createTrapTable :-" + e.getMessage());
		}
	}

	public void insertIntoTrapTable(TrapJsonDto trap, RaceJsonDto race, JsonNode jsonNode) {
		try {
			String query = "insert into trap (comments,greyhound_id,note,race_id,rank_no,sp,timed,times,prize) values(?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStmt = getConnection().prepareStatement(query);
			preparedStmt.setString(1, trap.getResultComment());
			preparedStmt.setLong(2, (trap.getDogId() == null) ? 0 : trap.getDogId());
			preparedStmt.setString(3, trap.getDogBorn() + " | " + trap.getResultDogWeight() + " | " + trap.getDogSex()
					+ "-" + trap.getDogColour() + " | " + trap.getDogSire() + " | " + trap.getDogDam());
			preparedStmt.setLong(4, race.getRaceId());
			preparedStmt.setLong(5, (trap.getResultPosition() == null) ? 0 : trap.getResultPosition());
			preparedStmt.setString(6, trap.getSP());
			if (trap.getResultRunTime() != null && trap.getResultAdjustedTime() != null) {
				preparedStmt.setString(7, trap.getResultRunTime() + "("
						+ getByValue(trap.getResultAdjustedTime(), trap.getResultRunTime()) + ")");
			} else {
				preparedStmt.setString(7, null);
			}
			preparedStmt.setString(8, trap.getResultSectionalTime());
			String[] prizes = race.getRacePrizes().split("\\|");
			for (String priize : prizes) {
				if (prizes[0].trim().isEmpty() || prizes[0] == null) {
					preparedStmt.setString(9, "0");
					break;
				} else if (String.valueOf(priize.trim().charAt(0)).trim()
						.equals(String.valueOf(trap.getResultPosition()).trim())) {
					preparedStmt.setString(9, priize.trim().split(" ")[1].trim().replaceAll("£", ""));
					break;

				} else if (priize.contains("Others")) {
					preparedStmt.setString(9, priize.trim().split(" ")[1].trim().replaceAll("£", ""));
					break;
				} else if (priize.contains("Total")) {
					preparedStmt.setString(9, "0");
					break;
				}
			}
			preparedStmt.execute();
			preparedStmt.close();
			logger.info("Data Insert Successfully in Trap table RaceID:-" + race.getRaceId());

		} catch (Exception e) {
			logger.error("Exception in insertIntoTrapTable :-" + e.getMessage() + " raceID:-" + race.getRaceId()
					+ " meeting-id:-" + jsonNode.get("meetingId").asLong());
		}
	}

	public void createGreyhoundTable() {
		try {
			Connection connection = getConnection();
			java.sql.Statement stmt = connection.createStatement();
			String sql = "CREATE TABLE if not exists greyhound" + "(id BIGINT not NULL AUTO_INCREMENT,"
					+ " birthday  varchar(255), " + " greyhound_id BIGINT, " + " name  varchar(255), "
					+ " stats varchar(255), " + " track  varchar(255), " + " trainer_id BIGINT,"
					+ " updatedAt datetime, " + " createdAt datetime, " + " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);
			logger.info("Greyhound table created successfully");
		} catch (Exception e) {
			logger.error("Exception in createGreyhoundTable :-" + e.getMessage());
		}
	}

	public void insertIntoGreyhoundTable(TrapJsonDto trap, JsonNode jsonNode, int trainerIdByName) {
		try {
			String query = "insert into greyhound (birthday,greyhound_id,name,stats,track,trainer_id) values(?,?,?,?,?,?)";
			PreparedStatement preparedStmt = getConnection().prepareStatement(query);
			preparedStmt.setString(1, trap.getDogBorn());
			preparedStmt.setLong(2, (trap.getDogId() == null) ? 0 : trap.getDogId());
			preparedStmt.setString(3, trap.getDogName());
			preparedStmt.setString(4,
					trap.getDogSire() + " - " + trap.getDogDam() + " | " + trap.getDogSex() + "-" + trap.getDogColour()
							+ " | " + trap.getDogBorn()
							+ ((trap.getDogSeason() == null) ? " | -" : " | " + trap.getDogSeason()));
			preparedStmt.setString(5, jsonNode.get("trackName").asText());
			preparedStmt.setLong(6, trainerIdByName);
			preparedStmt.execute();
			preparedStmt.close();
			logger.info("Data Insert Successfully in Greyhound table MeetingID:-" + jsonNode.get("meetingId").asLong());

		} catch (Exception e) {
			logger.error("Exception in insertIntoGreyhoundTable :-" + e.getMessage() + " MeetingID:-"
					+ jsonNode.get("meetingId").asLong() + " greyhound-id: " + trap.getDogId());
		}
	}

	public int getTrainerIdByName(String trainer) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			String q = "SELECT * FROM trainer WHERE name = ?";
			statement = connection.prepareStatement(q);
			statement.setString(1, trainer);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("id");
			}
		} catch (Exception e) {
			logger.error("Exception in getTrainerIdByName :-" + e.getMessage());
		} finally {
			try {
				statement.close();
				resultSet.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return 0;
	}

	public void createGreyhoundProfileTable() {
		try {
			Connection connection = getConnection();
			java.sql.Statement stmt = connection.createStatement();
			String sql = "CREATE TABLE if not exists greyhound_profile" + "(id BIGINT not NULL AUTO_INCREMENT,"
					+ " byy  varchar(255), " + " calc_tm varchar(255), " + " date  datetime, "
					+ " greyhound_id BIGINT, " + " meeting_id  BIGINT, " + " position BIGINT," + " race_id BIGINT, "
					+ " race_link  varchar(255), " + " remarks varchar(255)," + " sp varchar(255), "
					+ " stmhcp  varchar(255), " + " weight varchar(255)," + " win_time varchar(255),"
					+ " prize BIGINT, " + " updatedAt datetime, " + " createdAt datetime, " + " rating int(11), "
					+ " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);
			logger.info("createGreyhoundProfileTable table created successfully");
		} catch (Exception e) {

			logger.error("Exception in createGreyhoundProfileTable :-" + e.getMessage());
		}
	}

	public void insertIntoGreyhoundProfileTable(CopyOnWriteArrayList<GreyhoundProfileJsonDto> listOfprofiles,
			Long greyhoundID, String racePrize) {
		for (GreyhoundProfileJsonDto greyhoundProfile : listOfprofiles) {
			try {
				boolean checkExist = checkIsExistForGreyhoundProfileTable(greyhoundID, greyhoundProfile.getMeetingId(),
						greyhoundProfile.getRaceId(), "greyhound_profile");
				if (!checkExist) {
					String query = "insert into greyhound_profile (byy,calc_tm,date,greyhound_id,meeting_id,position,race_id,race_link,remarks,sp,stmhcp,weight,win_time,prize) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement preparedStmt = getConnection().prepareStatement(query);

					if (greyhoundProfile.getRaceWinTime() != null && greyhoundProfile.getResultRunTime() != null) {
						preparedStmt.setString(1,
								getByValue(greyhoundProfile.getRaceWinTime(), greyhoundProfile.getResultRunTime()));
					} else {
						preparedStmt.setString(1, null);
					}
					preparedStmt.setString(2, greyhoundProfile.getResultAdjustedTime());
					preparedStmt.setDate(3, new java.sql.Date(format.parse(greyhoundProfile.getRaceDate()).getTime()));
					preparedStmt.setLong(4, greyhoundID);
					preparedStmt.setLong(5, greyhoundProfile.getMeetingId());
					preparedStmt.setLong(6,
							(greyhoundProfile.getResultPosition() != null) ? greyhoundProfile.getResultPosition() : 0);
					preparedStmt.setLong(7, greyhoundProfile.getRaceId());
					preparedStmt.setString(8, "https://www.gbgb.org.uk/meeting/?meetingId="
							+ greyhoundProfile.getMeetingId() + "&raceId=" + greyhoundProfile.getRaceId());
					preparedStmt.setString(9, greyhoundProfile.getResultComment());
					preparedStmt.setString(10, greyhoundProfile.getSP());
					preparedStmt.setString(11, greyhoundProfile.getResultSectionalTime());
					preparedStmt.setString(12, greyhoundProfile.getResultDogWeight());
					preparedStmt.setString(13, greyhoundProfile.getRaceWinTime());
					String[] prizes = racePrize.split("\\|");
					for (String priize : prizes) {
						if (prizes[0].trim().isEmpty() || prizes[0] == null) {
							preparedStmt.setString(14, "0");
							break;
						} else if (String.valueOf(priize.trim().charAt(0)).trim()
								.equals(String.valueOf(greyhoundProfile.getResultPosition()).trim())) {
							preparedStmt.setString(14, priize.trim().split(" ")[1].trim().replaceAll("£", ""));
							break;
						} else if (priize.contains("Others")) {
							preparedStmt.setString(14, priize.trim().split(" ")[1].trim().replaceAll("£", ""));
							break;
						} else if (priize.contains("Total")) {
							preparedStmt.setString(14, "0");
							break;
						}

					}
					preparedStmt.execute();
					preparedStmt.close();
					logger.info("Data Insert Successfully in GreyHoundProfile table MeetingID:-"
							+ greyhoundProfile.getMeetingId());
				}
			} catch (Exception e) {
				logger.error("Exception in greyhound_profile :-" + e.getMessage() + " greyhound_id :- " + greyhoundID);
			}
		}
	}

	public void createTables() {
		try {
			createGreyhoundProfileTable();
			createGreyhoundTable();
			createMeetingTable();
			createRaceTable();
			createTrainerTable();
			createTrapTable();
		} catch (Exception e) {
			logger.error("Exception in createTables :-" + e.getMessage());
		}

	}

	public ResultSet getDistinctMeetingByDateAnTrack(LocalDate date, String track) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();
			String query = "SELECT DISTINCT meeting_id FROM meeting where date = '" + date + "' and track = '" + track
					+ "'";
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
			logger.error("Exception in getDistinctMeetingByDate:-" + e.getMessage());
		}

		return rs;
	}

	public ResultSet getAllRacesByMeetingID(long meeting_id) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();
			String query = "SELECT *  FROM race where meeting_id=" + meeting_id;
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
			logger.error("Exception in getAllRacesByMeetingID:-" + e.getMessage());
		}

		return rs;
	}

	public ResultSet getAllGreyhoundByMeetingIDAndRaceID(long meeting_id, long raceId) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();
			String query = "SELECT * FROM greyhound_profile where meeting_id=" + meeting_id + " AND race_id=" + raceId
					+ " order by calc_tm desc";
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
			logger.error("Exception in getAllGreyhoundByMeetingIDAndRaceID:-" + e.getMessage());
		}

		return rs;
	}

	public void updateRatingOfGreyhound(int rating, RatingDto ratingDto) {
		try {
			String query = "UPDATE greyhound_profile SET rating =? WHERE meeting_id=? AND race_id=? AND greyhound_id=?";
			PreparedStatement preparedStmt = getConnection().prepareStatement(query);
			preparedStmt.setInt(1, rating);
			preparedStmt.setLong(2, ratingDto.getMeetingId());
			preparedStmt.setLong(3, ratingDto.getRaceId());
			preparedStmt.setLong(4, ratingDto.getGreyhoundId());
			preparedStmt.execute();
			preparedStmt.close();
			logger.info("Rating update successfully for meeting_id :-" + ratingDto.getMeetingId() + " race_id :-"
					+ ratingDto.getRaceId() + " greyhound_id:-" + ratingDto.getGreyhoundId());
		} catch (Exception e) {
			logger.error("Exception in updateRatingOfGreyhound:-" + e.getMessage());
		}

	}

	public String getByValue(String runTime, String winTime) {
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

	public String decimalToFraction(double e) {
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

	public double findGCD(double a, double b) {
		if (b == 0) {
			return a;
		}
		return findGCD(b, a % b);
	}

	public ResultSet getDistinctGreyhoundId(LocalDate date, String track) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();
			String query = "SELECT DISTINCT meeting_id FROM meeting where date = '" + date + "' and track = '" + track
					+ "'";
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
			logger.error("Exception in getDistinctMeetingByDate:-" + e.getMessage());
		}

		return rs;
	}

	public void deleteGreyhoundById(Long id) {
		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();
			String sql = "DELETE FROM greyhound_profile WHERE id = " + id;
			stmt.executeUpdate(sql);
			logger.info("record deleted id:- " + id);
		} catch (Exception e) {
			logger.error("Exception in deleteGreyhoundById:-" + e.getMessage());
		}
	}

}
