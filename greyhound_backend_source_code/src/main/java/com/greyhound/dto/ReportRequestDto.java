package com.greyhound.dto;

import java.util.Date;

/**
 * 
 * @author p4logics
 *
 */
public class ReportRequestDto {
	private Date fromDate;
	private Date toDate;
	private String track;
	private Long trainer;
	private String grade;
	private int distance;
	private String order;

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

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public Long getTrainer() {
		return trainer;
	}

	public void setTrainer(Long trainer) {
		this.trainer = trainer;
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
