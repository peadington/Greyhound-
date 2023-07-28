package com.greyhound.dto;

import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RaceDto {

	private String raceTime;
	private String raceDate;
	private Long raceId;
	private String raceTitle;
	private Long raceNumber;
	private String raceType;
	private Boolean raceHandicap;
	private String raceClass;
	private Integer raceDistance;
	private String racePrizes;
	private Long raceGoing;
	private String raceForecast;
	private String raceTricast;
	private CopyOnWriteArrayList<TrapDto> traps;

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

	public Integer getRaceDistance() {
		return raceDistance;
	}

	public void setRaceDistance(Integer raceDistance) {
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

	public CopyOnWriteArrayList<TrapDto> getTraps() {
		return traps;
	}

	public void setTraps(CopyOnWriteArrayList<TrapDto> traps) {
		this.traps = traps;
	}

}
