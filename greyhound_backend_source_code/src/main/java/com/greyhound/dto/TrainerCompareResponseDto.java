package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class TrainerCompareResponseDto {
	private String date;
	private String rating;
	private String stmhcp;
	private String calcTime;
	private String winTime;
	private String totalIncome;
	private String totalWin;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getStmhcp() {
		return stmhcp;
	}

	public void setStmhcp(String stmhcp) {
		this.stmhcp = stmhcp;
	}

	public String getCalcTime() {
		return calcTime;
	}

	public void setCalcTime(String calcTime) {
		this.calcTime = calcTime;
	}

	public String getWinTime() {
		return winTime;
	}

	public void setWinTime(String winTime) {
		this.winTime = winTime;
	}

	public String getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}

	public String getTotalWin() {
		return totalWin;
	}

	public void setTotalWin(String totalWin) {
		this.totalWin = totalWin;
	}

}
