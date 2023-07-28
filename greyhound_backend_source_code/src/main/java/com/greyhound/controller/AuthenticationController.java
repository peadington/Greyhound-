package com.greyhound.controller;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.greyhound.config.JwtTokenUtil;
import com.greyhound.constant.AppConstant;
import com.greyhound.constant.Constants;
import com.greyhound.dto.ApiResponseDto;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.LoginResponseDto;
import com.greyhound.dto.LoginUser;
import com.greyhound.mapper.CustomMapper;
import com.greyhound.model.User;
import com.greyhound.service.IUserService;

/**
 * 
 * @author p4logics
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(AppConstant.API_BASE_URL + "/auth")
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private IUserService userService;

	@Autowired
	private CustomMapper customMapper;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ApiResponseDto login(@RequestBody LoginUser loginUser) throws AuthenticationException {
		logger.info("Login attempt....");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User userDetails = userService.findByEmail(loginUser.getUsername());
		if (userDetails != null) {
			try {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
				final UserDetails user = userDetailsService.loadUserByUsername(userDetails.getEmail());
				final String token = jwtTokenUtil.generateToken(user);
				LoginResponseDto loginResponseDto = customMapper.userToLoginResponseDto(userDetails);
				Map<String, Object> response = setTokenDetails(user, token, loginResponseDto);
				apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage(AppConstant.LOGIN_SUCESSFULL)
						.withData(response);
				logger.info("Login success!! Username is " + userDetails.getEmail());
			} catch (Exception e) {
				apiResponseDtoBuilder.withStatus(HttpStatus.UNAUTHORIZED)
						.withMessage(Constants.INVALID_USERNAME_OR_PASSWORD);
				logger.error("Login fail!! " + Constants.INVALID_USERNAME_OR_PASSWORD + "!! Username is "
						+ loginUser.getUsername());
			}
		} else {
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage(Constants.INVALID_USERNAME);
			logger.error("Login fail!! " + Constants.INVALID_USERNAME + "!! Username is " + loginUser.getUsername());
		}
		return apiResponseDtoBuilder.build();
	}

	private Map<String, Object> setTokenDetails(final UserDetails userDetails, final String token,
			final LoginResponseDto loginResponseDto) {
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstant.TOKEN, token);
		response.put(AppConstant.LOGINEDUSER, loginResponseDto);
		return response;
	}
}
