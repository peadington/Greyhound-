package com.greyhound.dto;

public class RatingDto {

	private Long meetingId;
	private Long raceId;
	private Long greyhoundId;
	private Double calculatedTime;

	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public Long getRaceId() {
		return raceId;
	}

	public void setRaceId(Long raceId) {
		this.raceId = raceId;
	}

	public Long getGreyhoundId() {
		return greyhoundId;
	}

	public void setGreyhoundId(Long greyhoundId) {
		this.greyhoundId = greyhoundId;
	}

	public Double getCalculatedTime() {
		return calculatedTime;
	}

	public void setCalculatedTime(Double calculatedTime) {
		this.calculatedTime = calculatedTime;
	}

}
