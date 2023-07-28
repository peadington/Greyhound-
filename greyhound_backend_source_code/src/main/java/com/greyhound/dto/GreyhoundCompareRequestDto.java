package com.greyhound.dto;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundCompareRequestDto {
	private String track;
	private List<Long> trainer;
	private List<Long> greyhound;
	private int distance;
	private String chartkind;
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

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public List<Long> getTrainer() {
		return trainer;
	}

	public void setTrainer(List<Long> trainer) {
		this.trainer = trainer;
	}

	public List<Long> getGreyhound() {
		return greyhound;
	}

	public void setGreyhound(List<Long> greyhound) {
		this.greyhound = greyhound;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getChartkind() {
		return chartkind;
	}

	public void setChartkind(String chartkind) {
		this.chartkind = chartkind;
	}

}
