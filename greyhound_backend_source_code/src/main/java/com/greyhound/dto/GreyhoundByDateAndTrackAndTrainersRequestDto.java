package com.greyhound.dto;

import java.util.Date;
import java.util.List;

public class GreyhoundByDateAndTrackAndTrainersRequestDto {
	private Date fromDate;
	private Date toDate;
	private List<String> track;
	private List<Long> trainers;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<String> getTrack() {
		return track;
	}

	public void setTrack(List<String> track) {
		this.track = track;
	}

	public List<Long> getTrainers() {
		return trainers;
	}

	public void setTrainers(List<Long> trainers) {
		this.trainers = trainers;
	}

}
