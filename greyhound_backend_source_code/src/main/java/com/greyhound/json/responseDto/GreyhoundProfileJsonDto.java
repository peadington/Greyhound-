package com.greyhound.json.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GreyhoundProfileJsonDto {

	@JsonProperty("SP")
	private String SP;

	private Long resultPosition;
	private String resultBtnDistance;
	private String resultSectionalTime;
	private String resultComment;
	private String resultRunTime;
	private String resultDogWeight;
	private String winnerOr2ndName;
	private Long winnerOr2ndId;
	private String resultAdjustedTime;
	private String trapNumber;
	private String raceTime;
	private String trackName;
	private String raceDate;
	private Long raceId;
	private String raceType;
	private String raceClass;
	private Double raceDistance;
	private String raceGoing;
	private String raceWinTime;
	private Long meetingId;

	public String getSP() {
		return SP;
	}

	public void setSP(String sP) {
		SP = sP;
	}

	public Long getResultPosition() {
		return resultPosition;
	}

	public void setResultPosition(Long resultPosition) {
		this.resultPosition = resultPosition;
	}

	public String getResultBtnDistance() {
		return resultBtnDistance;
	}

	public void setResultBtnDistance(String resultBtnDistance) {
		this.resultBtnDistance = resultBtnDistance;
	}

	public String getResultSectionalTime() {
		return resultSectionalTime;
	}

	public void setResultSectionalTime(String resultSectionalTime) {
		this.resultSectionalTime = resultSectionalTime;
	}

	public String getResultComment() {
		return resultComment;
	}

	public void setResultComment(String resultComment) {
		this.resultComment = resultComment;
	}

	public String getResultRunTime() {
		return resultRunTime;
	}

	public void setResultRunTime(String resultRunTime) {
		this.resultRunTime = resultRunTime;
	}

	public String getResultDogWeight() {
		return resultDogWeight;
	}

	public void setResultDogWeight(String resultDogWeight) {
		this.resultDogWeight = resultDogWeight;
	}

	public String getWinnerOr2ndName() {
		return winnerOr2ndName;
	}

	public void setWinnerOr2ndName(String winnerOr2ndName) {
		this.winnerOr2ndName = winnerOr2ndName;
	}

	public Long getWinnerOr2ndId() {
		return winnerOr2ndId;
	}

	public void setWinnerOr2ndId(Long winnerOr2ndId) {
		this.winnerOr2ndId = winnerOr2ndId;
	}

	public String getResultAdjustedTime() {
		return resultAdjustedTime;
	}

	public void setResultAdjustedTime(String resultAdjustedTime) {
		this.resultAdjustedTime = resultAdjustedTime;
	}

	public String getTrapNumber() {
		return trapNumber;
	}

	public void setTrapNumber(String trapNumber) {
		this.trapNumber = trapNumber;
	}

	public String getRaceTime() {
		return raceTime;
	}

	public void setRaceTime(String raceTime) {
		this.raceTime = raceTime;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getRaceDate() {
		return raceDate;
	}

	public void setRaceDate(String raceDate) {
		this.raceDate = raceDate;
	}

	public Long getRaceId() {
		return raceId;
	}

	public void setRaceId(Long raceId) {
		this.raceId = raceId;
	}

	public String getRaceType() {
		return raceType;
	}

	public void setRaceType(String raceType) {
		this.raceType = raceType;
	}

	public String getRaceClass() {
		return raceClass;
	}

	public void setRaceClass(String raceClass) {
		this.raceClass = raceClass;
	}

	public Double getRaceDistance() {
		return raceDistance;
	}

	public void setRaceDistance(Double raceDistance) {
		this.raceDistance = raceDistance;
	}

	public String getRaceGoing() {
		return raceGoing;
	}

	public void setRaceGoing(String raceGoing) {
		this.raceGoing = raceGoing;
	}

	public String getRaceWinTime() {
		return raceWinTime;
	}

	public void setRaceWinTime(String raceWinTime) {
		this.raceWinTime = raceWinTime;
	}

	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

}
