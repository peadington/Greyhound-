package com.greyhound.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingDto {

	private Long resultPosition;
	private Long trapNumber;
	private String raceTime;
	private String raceDate;
	private Long raceId;
	private Long raceNumber;
	private String raceClass;
	private Double raceDistance;
	private Long meetingId;
	private String trackName;
	private Long dogId;
	private String dogName;
	private String dogSire;
	private String dogDam;
	private String trainerName;
	private String ownerName;

	public Long getResultPosition() {
		return resultPosition;
	}

	public void setResultPosition(Long resultPosition) {
		this.resultPosition = resultPosition;
	}

	public Long getTrapNumber() {
		return trapNumber;
	}

	public void setTrapNumber(Long trapNumber) {
		this.trapNumber = trapNumber;
	}

	public String getRaceTime() {
		return raceTime;
	}

	public void setRaceTime(String raceTime) {
		this.raceTime = raceTime;
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

	public Long getRaceNumber() {
		return raceNumber;
	}

	public void setRaceNumber(Long raceNumber) {
		this.raceNumber = raceNumber;
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

	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public Long getDogId() {
		return dogId;
	}

	public void setDogId(Long dogId) {
		this.dogId = dogId;
	}

	public String getDogName() {
		return dogName;
	}

	public void setDogName(String dogName) {
		this.dogName = dogName;
	}

	public String getDogSire() {
		return dogSire;
	}

	public void setDogSire(String dogSire) {
		this.dogSire = dogSire;
	}

	public String getDogDam() {
		return dogDam;
	}

	public void setDogDam(String dogDam) {
		this.dogDam = dogDam;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Override
	public String toString() {
		return "Meeting [resultPosition=" + resultPosition + ", trapNumber=" + trapNumber + ", raceTime=" + raceTime
				+ ", raceDate=" + raceDate + ", raceId=" + raceId + ", raceNumber=" + raceNumber + ", raceClass="
				+ raceClass + ", raceDistance=" + raceDistance + ", meetingId=" + meetingId + ", trackName=" + trackName
				+ ", dogId=" + dogId + ", dogName=" + dogName + ", dogSire=" + dogSire + ", dogDam=" + dogDam
				+ ", trainerName=" + trainerName + ", ownerName=" + ownerName + "]";
	}

}
