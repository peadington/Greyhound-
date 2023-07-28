package com.greyhound.service;

import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.model.User;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IUserService {

	void sendTemporaryPassword(String email, int otp, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void changePassword(String email, ApiResponseDtoBuilder apiResponseDtoBuilder);

	User findByEmail(String username);

	User findById(Long id);

	User getSessionUser();

}
