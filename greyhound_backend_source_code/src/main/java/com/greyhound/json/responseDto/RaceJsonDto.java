package com.greyhound.json.responseDto;

import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RaceJsonDto {

	private String raceTime;
	private String raceDate;
	private Long raceId;
	private String raceTitle;
	private Long raceNumber;
	private String raceType;
	private Boolean raceHandicap;
	private String raceClass;
	private Double raceDistance;
	private String racePrizes;
	private Long raceGoing;
	private String raceForecast;
	private String raceTricast;
	private CopyOnWriteArrayList<TrapJsonDto> traps;

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

	public String getRaceTitle() {
		return raceTitle;
	}

	public void setRaceTitle(String raceTitle) {
		this.raceTitle = raceTitle;
	}

	public Long getRaceNumber() {
		return raceNumber;
	}

	public void setRaceNumber(Long raceNumber) {
		this.raceNumber = raceNumber;
	}

	public String getRaceType() {
		return raceType;
	}

	public void setRaceType(String raceType) {
		this.raceType = raceType;
	}

	public Boolean getRaceHandicap() {
		return raceHandicap;
	}

	public void setRaceHandicap(Boolean raceHandicap) {
		this.raceHandicap = raceHandicap;
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

	public String getRacePrizes() {
		return racePrizes;
	}

	public void setRacePrizes(String racePrizes) {
		this.racePrizes = racePrizes;
	}

	public Long getRaceGoing() {
		return raceGoing;
	}

	public void setRaceGoing(Long raceGoing) {
		this.raceGoing = raceGoing;
	}

	public String getRaceForecast() {
		return raceForecast;
	}

	public void setRaceForecast(String raceForecast) {
		this.raceForecast = raceForecast;
	}

	public String getRaceTricast() {
		return raceTricast;
	}

	public void setRaceTricast(String raceTricast) {
		this.raceTricast = raceTricast;
	}

	public CopyOnWriteArrayList<TrapJsonDto> getTraps() {
		return traps;
	}

	public void setTraps(CopyOnWriteArrayList<TrapJsonDto> traps) {
		this.traps = traps;
	}

}
