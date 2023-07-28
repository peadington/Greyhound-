package com.greyhound.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.greyhound.constant.AppConstant;
import com.greyhound.constant.Constants;
import com.greyhound.constant.EmailConstant;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.model.User;
import com.greyhound.repository.UserRepository;
import com.greyhound.service.IEmailService;
import com.greyhound.service.IOtpService;
import com.greyhound.service.IUserService;
import com.greyhound.utility.Utility;

/**
 * 
 * @author p4logics
 *
 */
@Service("userService")
public class UserServiceImpl implements IUserService, UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private IOtpService otpService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(Constants.INVALID_USERNAME);
		}
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority(AppConstant.ROLE_ADMIN));
	}

	@Override
	public void sendTemporaryPassword(String email, int otp, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		User user = findByEmail(email);
		if (user == null) {
			apiResponseDtoBuilder.withMessage(Constants.NO_EMAIL_EXISTS).withStatus(HttpStatus.NOT_FOUND);
			logger.error("Forgot password! Email not found!! Email is : " + email);
			return;
		}
		if (otpService.verifyOtp(email, otp, apiResponseDtoBuilder)) {
			String password = Utility.generateRandomPassword(8);
			String newPasswordEncodedString = bcryptEncoder.encode(password);
			user.setPassword(newPasswordEncodedString);
			save(user);
			apiResponseDtoBuilder.withMessage(Constants.SEND_DETAILS_TO_YOUR_EMAIL).withStatus(HttpStatus.OK);
			logger.info("Forgot password! Your temporary password has been sent to your email address!! Email is : "
					+ email);
			String subject = EmailConstant.EMAIL_SUBJECT_FOR_FORGOT_PASSWORD;
			String body = emailService.createEmailBodyForForgotPassword(user.getName(), password);
			emailService.sendEmail(user.getEmail(), subject, body, EmailConstant.EMAIL_LABEL_FOR_FORGOT_PASSWORD, null,
					null);
		}
	}

	@Override
	public void changePassword(String password, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		User user = getSessionUser();
		try {
			String newPasswordEncodedString = bcryptEncoder.encode(password);
			user.setPassword(newPasswordEncodedString);
			user.setUpdatedAt(new Date());
			save(user);
			apiResponseDtoBuilder.withMessage(Constants.PASSWORD_CHANGED).withStatus(HttpStatus.OK);
			logger.info("Change Password!! Change password successfully!!");
			String subject = "Password Change";
			String body = emailService.createEmailBodyForChangePassword(user.getName());
			emailService.sendEmail(user.getEmail(), subject, body, EmailConstant.EMAIL_LABEL_FOR_CHANGE_PASSWORD, null,
					null);
		} catch (Exception e) {
			apiResponseDtoBuilder.withMessage(Constants.SOMETHING_WENT_WRONG).withStatus(HttpStatus.BAD_REQUEST);
			logger.error("Change password!! Bad request!!");
		}
	}

	@Override
	public User getSessionUser() {
		return Utility.getSessionUser(userRepository);
	}

	@Override
	public User findByEmail(String username) {
		return userRepository.findByEmail(username);
	}

	private boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.isPresent() ? user.get() : null;
	}

	private void save(User user) {
		userRepository.save(user);
	}
}
