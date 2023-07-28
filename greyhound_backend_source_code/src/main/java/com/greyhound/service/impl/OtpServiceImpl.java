package com.greyhound.service.impl;

import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greyhound.constant.AppConstant;
import com.greyhound.constant.Constants;
import com.greyhound.constant.EmailConstant;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.model.Otp;
import com.greyhound.model.User;
import com.greyhound.repository.OtpRepository;
import com.greyhound.service.IEmailService;
import com.greyhound.service.IOtpService;
import com.greyhound.service.IUserService;
import com.greyhound.utility.Utility;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class OtpServiceImpl implements IOtpService {

	private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private OtpRepository otpRepository;

	@Override
	public void sendOtp(@Valid String email, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		User user = userService.findByEmail(email);
		if (user != null) {
			Integer otpValue = Utility.generateOTP();
			Otp otp = new Otp();
			otp.setEmail(email);
			otp.setDate(new Date().getTime());
			otp.setOtp(otpValue);
			otpRepository.save(otp);
			String subject = EmailConstant.EMAIL_SUBJECT_FOR_OTP;
			String body = emailService.createEmailBodyForOtp(user.getName(), otpValue);
			emailService.sendEmail(user.getEmail(), subject, body, EmailConstant.EMAIL_LABEL_FOR_OTP, null, null);
			apiResponseDtoBuilder.withMessage(Constants.OTP_SENT_SUCCESS).withStatus(HttpStatus.OK);
			logger.info("Sent OTP Success!! OTP sent successfully to " + user.getEmail());
		} else {
			apiResponseDtoBuilder.withMessage(Constants.USER_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
			logger.error("Sent OTP Fail!! User not found!! Email is " + email);
		}
	}

	@Override
	public boolean verifyOtp(@Valid String token, @Valid int otp, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Otp otpDb = otpRepository.findFirstByTokenAndOtpOrderByIdDesc(token, otp);
		if (otpDb != null) {
			long minutes = AppConstant.OTO_VALIDITY_MINUTES;
			long tempMili = new Date().getTime() - otpDb.getDate();
			if (minutes > tempMili) {
				if (!otpDb.getStatus()) {
					otpDb.setStatus(true);
					otpRepository.save(otpDb);
					apiResponseDtoBuilder.withMessage(Constants.VALID_OTP).withStatus(HttpStatus.OK);
					logger.info("Verify OTP Success!! OTP verified successfully!! " + otpDb.getEmail());
					return true;
				} else {
					apiResponseDtoBuilder.withMessage(Constants.OTP_VERIFY_SESSION_EXPIRE)
							.withStatus(HttpStatus.BAD_REQUEST);
					logger.error("Verify OTP Fail!! OTP session expired!!");
					return false;
				}
			} else {
				apiResponseDtoBuilder.withMessage(Constants.OTP_VERIFY_SESSION_EXPIRE)
						.withStatus(HttpStatus.BAD_REQUEST);
				logger.error("Verify OTP Fail!! OTP session expired!!");
				return false;
			}
		} else {
			apiResponseDtoBuilder.withMessage(Constants.INVALID_OTP).withStatus(HttpStatus.NOT_FOUND);
			logger.error("Verify OTP Fail!! Invalid OTP!!");
			return false;
		}
	}

	@Override
	public Otp generateOtp(String email) {
		Integer otpValue = Utility.generateOTP();
		Otp otp = new Otp();
		otp.setCreatedAt(new Date());
		otp.setEmail(email);
		otp.setDate(new Date().getTime());
		otp.setOtp(otpValue);
		UUID uuid = UUID.randomUUID();
		otp.setToken(uuid.toString());
		otpRepository.save(otp);
		return otp;
	}

	@Override
	public Otp findByTokenAndOtp(String token, int otp) {
		return otpRepository.findByTokenAndOtp(token, otp);
	}
}
