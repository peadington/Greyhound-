package com.greyhound.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 
 * @author p4logics
 *
 */
public class UserRequestDto {
	@NotEmpty
	@Size(min = 2, message = "Name should have at least 2 characters")
	private String name;

	@NotEmpty
	@NotEmpty(message = "Please enter your mobile number")
	@Size(min = 10, max = 10, message = "Mobile number can only be 10 digits")
	private String mobileNumber;

	@NotEmpty
	@Email
	private String email;

	@NotEmpty
	@Size(min = 8, message = "Password should have at least 8 characters")
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
