package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundResponseDtoForTrackAndName {
	private long greyhoundId;
	private String track;
	private String name;
	private String birthday;
	private String trainer;
	private String greyhoundUrl;
	private String raceUrl;

	public String getGreyhoundUrl() {
		return greyhoundUrl;
	}

	public void setGreyhoundUrl(String greyhoundUrl) {
		this.greyhoundUrl = greyhoundUrl;
	}

	public String getRaceUrl() {
		return raceUrl;
	}

	public void setRaceUrl(String raceUrl) {
		this.raceUrl = raceUrl;
	}

	public long getGreyhoundId() {
		return greyhoundId;
	}

	public void setGreyhoundId(long greyhoundId) {
		this.greyhoundId = greyhoundId;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getTrainer() {
		return trainer;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

}
