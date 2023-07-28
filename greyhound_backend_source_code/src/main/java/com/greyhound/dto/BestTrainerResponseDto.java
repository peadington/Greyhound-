package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class BestTrainerResponseDto {
	private int rank;
	private String trainerName;
	private int winerNumber;
	private double rankAverage;
	private int totalPrizes;
	private String note;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public int getWinerNumber() {
		return winerNumber;
	}

	public void setWinerNumber(int winerNumber) {
		this.winerNumber = winerNumber;
	}

	public double getRankAverage() {
		return rankAverage;
	}

	public void setRankAverage(double rankAverage) {
		this.rankAverage = rankAverage;
	}

	public int getTotalPrizes() {
		return totalPrizes;
	}

	public void setTotalPrizes(int totalPrizes) {
		this.totalPrizes = totalPrizes;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
