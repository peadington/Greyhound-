package com.greyhound.dto;

import java.util.List;

/**
 * 
 * @author p4logics
 *
 */
public class TrainerCompareRequestDto {
	private String track;
	private List<Long> trainer;
	private int days;

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

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

}
