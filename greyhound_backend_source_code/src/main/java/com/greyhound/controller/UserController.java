package com.greyhound.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greyhound.constant.AppConstant;
import com.greyhound.dto.ApiResponseDto;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.service.IUserService;

/**
 * 
 * @author p4logics
 *
 */
@CrossOrigin(origins = "*", maxAge = 360000000)
@RestController
@RequestMapping(AppConstant.API_BASE_URL)
public class UserController {

	@Autowired
	private IUserService userService;

	/**
	 * Forgot Password
	 * 
	 * @param email
	 * @param otp
	 * @return
	 */
	@RequestMapping(value = "/user/password/forgot", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto forgotPassword(@RequestParam(required = true) String email,
			@RequestParam(required = true) int otp) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		userService.sendTemporaryPassword(email, otp, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	/**
	 * Change Password
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/user/password/change", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto changePassword(@RequestParam(required = true) String password) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		userService.changePassword(password, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
}
