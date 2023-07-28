package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class TrainerReportResponseDto {
	private int rank;
	private String trainerName;
	private long winnerNum;
	private double avgRank;
	private long totalPrizes;
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

	public long getWinnerNum() {
		return winnerNum;
	}

	public void setWinnerNum(long winnerNum) {
		this.winnerNum = winnerNum;
	}

	public double getAvgRank() {
		return avgRank;
	}

	public void setAvgRank(double avgRank) {
		this.avgRank = avgRank;
	}

	public long getTotalPrizes() {
		return totalPrizes;
	}

	public void setTotalPrizes(long totalPrizes) {
		this.totalPrizes = totalPrizes;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
