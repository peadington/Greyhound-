package com.greyhound.json.responseDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrapJsonDto {

	private Long trapNumber;
	private String trapHandicap;
	private Long dogId;
	private String dogName;
	private String dogSire;
	private String dogDam;
	private String dogBorn;
	private String dogColour;
	private String dogSex;
	private String dogSeason;
	private String trainerName;
	private String ownerName;
	@JsonProperty("SP")
	private String SP;
	private Long resultPosition;
	private Long resultMarketPos;
	private Long resultMarketCnt;
	private Long resultPriceNumerator;
	private Long resultPriceDenominator;
	private String resultBtnDistance;
	private String resultSectionalTime;
	private String resultComment;
	private String resultRunTime;
	private String resultDogWeight;
	private String resultAdjustedTime;

	public Long getTrapNumber() {
		return trapNumber;
	}

	public void setTrapNumber(Long trapNumber) {
		this.trapNumber = trapNumber;
	}

	public String getTrapHandicap() {
		return trapHandicap;
	}

	public void setTrapHandicap(String trapHandicap) {
		this.trapHandicap = trapHandicap;
	}

	public Long getDogId() {
		return dogId;
	}

	public void setDogId(Long dogId) {
		this.dogId = dogId;
	}

	public String getDogName() {
		return dogName;
	}

	public void setDogName(String dogName) {
		this.dogName = dogName;
	}

	public String getDogSire() {
		return dogSire;
	}

	public void setDogSire(String dogSire) {
		this.dogSire = dogSire;
	}

	public String getDogDam() {
		return dogDam;
	}

	public void setDogDam(String dogDam) {
		this.dogDam = dogDam;
	}

	public String getDogBorn() {
		return dogBorn;
	}

	public void setDogBorn(String dogBorn) {
		this.dogBorn = dogBorn;
	}

	public String getDogColour() {
		return dogColour;
	}

	public void setDogColour(String dogColour) {
		this.dogColour = dogColour;
	}

	public String getDogSex() {
		return dogSex;
	}

	public void setDogSex(String dogSex) {
		this.dogSex = dogSex;
	}

	public String getDogSeason() {
		return dogSeason;
	}

	public void setDogSeason(String dogSeason) {
		this.dogSeason = dogSeason;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getSP() {
		return SP;
	}

	public void setSP(String sP) {
		SP = sP;
	}

	public Long getResultPosition() {
		return resultPosition;
	}

	public void setResultPosition(Long resultPosition) {
		this.resultPosition = resultPosition;
	}

	public Long getResultMarketPos() {
		return resultMarketPos;
	}

	public void setResultMarketPos(Long resultMarketPos) {
		this.resultMarketPos = resultMarketPos;
	}

	public Long getResultMarketCnt() {
		return resultMarketCnt;
	}

	public void setResultMarketCnt(Long resultMarketCnt) {
		this.resultMarketCnt = resultMarketCnt;
	}

	public Long getResultPriceNumerator() {
		return resultPriceNumerator;
	}

	public void setResultPriceNumerator(Long resultPriceNumerator) {
		this.resultPriceNumerator = resultPriceNumerator;
	}

	public Long getResultPriceDenominator() {
		return resultPriceDenominator;
	}

	public void setResultPriceDenominator(Long resultPriceDenominator) {
		this.resultPriceDenominator = resultPriceDenominator;
	}

	public String getResultBtnDistance() {
		return resultBtnDistance;
	}

	public void setResultBtnDistance(String resultBtnDistance) {
		this.resultBtnDistance = resultBtnDistance;
	}

	public String getResultSectionalTime() {
		return resultSectionalTime;
	}

	public void setResultSectionalTime(String resultSectionalTime) {
		this.resultSectionalTime = resultSectionalTime;
	}

	public String getResultComment() {
		return resultComment;
	}

	public void setResultComment(String resultComment) {
		this.resultComment = resultComment;
	}

	public String getResultRunTime() {
		return resultRunTime;
	}

	public void setResultRunTime(String resultRunTime) {
		this.resultRunTime = resultRunTime;
	}

	public String getResultDogWeight() {
		return resultDogWeight;
	}

	public void setResultDogWeight(String resultDogWeight) {
		this.resultDogWeight = resultDogWeight;
	}

	public String getResultAdjustedTime() {
		return resultAdjustedTime;
	}

	public void setResultAdjustedTime(String resultAdjustedTime) {
		this.resultAdjustedTime = resultAdjustedTime;
	}

}
