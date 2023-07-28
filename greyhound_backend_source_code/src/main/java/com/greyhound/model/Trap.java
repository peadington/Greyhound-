package com.greyhound.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greyhound.constant.TableConstant;

/**
 * 
 * @author p4logics
 *
 */
@Entity
@Table(name = TableConstant.TRAP_TABLE, indexes = @Index(columnList = "raceId,rankNo,greyhoundId"))
@JsonIgnoreProperties
public class Trap {

	public static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long raceId;
	private Integer rankNo;
	private Long greyhoundId;

	private String comments;
	private String sp;
	@Column(length = 10)
	private String timeS;
	@Column(length = 50)
	private String timeD;

	private String note;

	private Integer prize;

	public Integer getPrize() {
		return prize;
	}

	public void setPrize(Integer prize) {
		this.prize = prize;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRaceId() {
		return raceId;
	}

	public void setRaceId(Long raceId) {
		this.raceId = raceId;
	}

	public Integer getRankNo() {
		return rankNo;
	}

	public void setRankNo(Integer rankNo) {
		this.rankNo = rankNo;
	}

	public Long getGreyhoundId() {
		return greyhoundId;
	}

	public void setGreyhoundId(Long greyhoundId) {
		this.greyhoundId = greyhoundId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public String getTimeS() {
		return timeS;
	}

	public void setTimeS(String timeS) {
		this.timeS = timeS;
	}

	public String getTimeD() {
		return timeD;
	}

	public void setTimeD(String timeD) {
		this.timeD = timeD;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
