package com.greyhound.dto;

/**
 * 
 * @author p4logics
 *
 */
public class LoginUserVerifyDto {

	private String token;
	private int otp;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

}
