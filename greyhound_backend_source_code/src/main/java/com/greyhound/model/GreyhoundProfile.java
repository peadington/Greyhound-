package com.greyhound.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.greyhound.config.CustomDateAndTimeDeserialize;
import com.greyhound.config.CustomJsonDateSerializer;
import com.greyhound.constant.TableConstant;

/**
 * 
 * @author p4logics
 *
 */
@Entity
@Table(name = TableConstant.GREYHOUND_PROFILE_TABLE, indexes = @Index(columnList = "greyhoundId,stmhcp,position,winTime,weight,calcTm,rating,prize"))
@JsonIgnoreProperties
public class GreyhoundProfile {

	public static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long greyhoundId;
	@Column(length = 10)
	private String stmhcp;
	private Integer position;
	private String byy;
	private String remarks;
	@Column(length = 10)
	private String winTime;
	@Column(length = 10)
	private String weight;
	private String sp;
	@Column(length = 10)
	private String calcTm;
	private String raceLink;
	private Long meetingId;
	private Long raceId;
	private Integer rating;
	private Integer prize;

	@JsonSerialize(using = CustomJsonDateSerializer.class)
	@JsonDeserialize(using = CustomDateAndTimeDeserialize.class)
	private Date date;

	public Integer getPrize() {
		return prize;
	}

	public void setPrize(Integer prize) {
		this.prize = prize;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGreyhoundId() {
		return greyhoundId;
	}

	public void setGreyhoundId(Long greyhoundId) {
		this.greyhoundId = greyhoundId;
	}

	public String getStmhcp() {
		return stmhcp;
	}

	public void setStmhcp(String stmhcp) {
		this.stmhcp = stmhcp;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getByy() {
		return byy;
	}

	public void setByy(String byy) {
		this.byy = byy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWinTime() {
		return winTime;
	}

	public void setWinTime(String winTime) {
		this.winTime = winTime;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public String getCalcTm() {
		return calcTm;
	}

	public void setCalcTm(String calcTm) {
		this.calcTm = calcTm;
	}

	public String getRaceLink() {
		return raceLink;
	}

	public void setRaceLink(String raceLink) {
		this.raceLink = raceLink;
	}

	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public Long getRaceId() {
		return raceId;
	}

	public void setRaceId(Long raceId) {
		this.raceId = raceId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
