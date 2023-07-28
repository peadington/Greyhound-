package com.greyhound.dto;

import java.util.Date;

/**
 * 
 * @author p4logics
 *
 */
public class TrainerFilterDto {
	private long trainerId;
	private String track;
	private int distance;
	private String grade;
	private String chartKind;
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

	public long getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(long trainerId) {
		this.trainerId = trainerId;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getChartKind() {
		return chartKind;
	}

	public void setChartKind(String chartKind) {
		this.chartKind = chartKind;
	}

}
