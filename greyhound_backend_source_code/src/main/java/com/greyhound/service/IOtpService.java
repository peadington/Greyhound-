package com.greyhound.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.model.Otp;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IOtpService {

	void sendOtp(@Valid String email, ApiResponseDtoBuilder apiResponseDtoBuilder);

	boolean verifyOtp(@Valid String email, @Valid int otp, ApiResponseDtoBuilder apiResponseDtoBuilder);

	Otp generateOtp(String email);

	Otp findByTokenAndOtp(String token, int otp);

}
