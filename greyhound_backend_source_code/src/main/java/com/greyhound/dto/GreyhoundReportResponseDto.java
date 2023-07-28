package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class GreyhoundReportResponseDto {
	private String date;
	private String track;
	private long distance;
	private String grade;
	private Double sTmHcp;
	private long position;
	private Double winTime;
	private Double weight;
	private Double calcTm;
	private String rating;
	private Double prizeMoney;
	private String comment;
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Double getsTmHcp() {
		return sTmHcp;
	}

	public void setsTmHcp(Double sTmHcp) {
		this.sTmHcp = sTmHcp;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public Double getWinTime() {
		return winTime;
	}

	public void setWinTime(Double winTime) {
		this.winTime = winTime;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getCalcTm() {
		return calcTm;
	}

	public void setCalcTm(Double calcTm) {
		this.calcTm = calcTm;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Double getPrizeMoney() {
		return prizeMoney;
	}

	public void setPrizeMoney(Double prizeMoney) {
		this.prizeMoney = prizeMoney;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
