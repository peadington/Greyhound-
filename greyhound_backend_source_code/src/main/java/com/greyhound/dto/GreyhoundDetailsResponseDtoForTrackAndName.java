package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundDetailsResponseDtoForTrackAndName {
	private String date;
	private int distance;
	private int position;
	private String weight;
	private String winTime;
	private String sTmHcp;
	private String calcTm;
	private String raceUrl;

	public String getRaceUrl() {
		return raceUrl;
	}

	public void setRaceUrl(String raceUrl) {
		this.raceUrl = raceUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWinTime() {
		return winTime;
	}

	public void setWinTime(String winTime) {
		this.winTime = winTime;
	}

	public String getsTmHcp() {
		return sTmHcp;
	}

	public void setsTmHcp(String sTmHcp) {
		this.sTmHcp = sTmHcp;
	}

	public String getCalcTm() {
		return calcTm;
	}

	public void setCalcTm(String calcTm) {
		this.calcTm = calcTm;
	}

}
