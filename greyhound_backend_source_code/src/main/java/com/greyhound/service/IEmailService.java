package com.greyhound.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.greyhound.model.User;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IEmailService {

	void sendEmail(String toEmail, String subject, String body, String label, List<String> attachmentUrlList,
			List<String> ccEmailList);

	String createEmailBodyForNewUser(User user, String password);

	String createEmailBodyForChangePassword(String name);

	String createEmailBodyForForgotPassword(String name, String password);

	String createEmailBodyForOtp(String name, int otp);

	String createEmailBodyForLogin(User userDetails, Integer otp);

}
