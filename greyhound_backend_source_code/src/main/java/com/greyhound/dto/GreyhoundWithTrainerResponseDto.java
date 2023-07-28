package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundWithTrainerResponseDto {
	private long greyhoundId;
	private String greyhoundName;
	private long trainerId;
	private String trainerName;
	private String stats;
	private String track;

	public long getGreyhoundId() {
		return greyhoundId;
	}

	public void setGreyhoundId(long greyhoundId) {
		this.greyhoundId = greyhoundId;
	}

	public String getGreyhoundName() {
		return greyhoundName;
	}

	public void setGreyhoundName(String greyhoundName) {
		this.greyhoundName = greyhoundName;
	}

	public long getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(long trainerId) {
		this.trainerId = trainerId;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getStats() {
		return stats;
	}

	public void setStats(String stats) {
		this.stats = stats;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

}
