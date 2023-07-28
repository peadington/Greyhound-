package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundCompareResponseDto {
	private String greyhoundName;
	private double rating;
	private double stmhcp;
	private double calcTime;
	private double winTime;

	public String getGreyhoundName() {
		return greyhoundName;
	}

	public void setGreyhoundName(String greyhoundName) {
		this.greyhoundName = greyhoundName;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getStmhcp() {
		return stmhcp;
	}

	public void setStmhcp(double stmhcp) {
		this.stmhcp = stmhcp;
	}

	public double getCalcTime() {
		return calcTime;
	}

	public void setCalcTime(double calcTime) {
		this.calcTime = calcTime;
	}

	public double getWinTime() {
		return winTime;
	}

	public void setWinTime(double winTime) {
		this.winTime = winTime;
	}

}
