package com.greyhound.dto;

import java.util.Date;

/**
 * 
 * @author p4logics
 *
 */
public class UserListResponseDto {
	private String id;
	private Date createdAt;
	private String name;
	private String email;
	private String mobileNumber;
	private ImageDto profileImage;
	private Boolean active;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ImageDto getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(ImageDto profileImage) {
		this.profileImage = profileImage;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
