package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class ReportResponseDto {
	private Long greyhoundId;
	private String greyhoundName;
	private long entryNum;
	private double stmhcp;
	private String winTime;
	private double weight;
	private double calcTm;
	private String note;
	private double rating;
	private double prize;

	public Long getGreyhoundId() {
		return greyhoundId;
	}

	public void setGreyhoundId(Long greyhoundId) {
		this.greyhoundId = greyhoundId;
	}

	public double getPrize() {
		return prize;
	}

	public void setPrize(double prize) {
		this.prize = prize;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getGreyhoundName() {
		return greyhoundName;
	}

	public void setGreyhoundName(String greyhoundName) {
		this.greyhoundName = greyhoundName;
	}

	public long getEntryNum() {
		return entryNum;
	}

	public void setEntryNum(long entryNum) {
		this.entryNum = entryNum;
	}

	public double getStmhcp() {
		return stmhcp;
	}

	public void setStmhcp(double stmhcp) {
		this.stmhcp = stmhcp;
	}

	public String getWinTime() {
		return winTime;
	}

	public void setWinTime(String winTime) {
		this.winTime = winTime;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getCalcTm() {
		return calcTm;
	}

	public void setCalcTm(double calcTm) {
		this.calcTm = calcTm;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
