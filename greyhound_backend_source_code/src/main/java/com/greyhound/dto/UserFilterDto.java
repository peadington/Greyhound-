package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class UserFilterDto {
	private String keyword;
	private Boolean active;
	private int role;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

}
