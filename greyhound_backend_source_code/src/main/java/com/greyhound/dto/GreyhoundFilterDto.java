package com.greyhound.dto;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundFilterDto {
	private List<String> track;
	private Long trainerId;
	private Long greyhoundId;
	private String grade;
	private int distance;
	private Date fromDate;
	private Date toDate;

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

	public Long getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(Long trainerId) {
		this.trainerId = trainerId;
	}

	public Long getGreyhoundId() {
		return greyhoundId;
	}

	public void setGreyhoundId(Long greyhoundId) {
		this.greyhoundId = greyhoundId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
