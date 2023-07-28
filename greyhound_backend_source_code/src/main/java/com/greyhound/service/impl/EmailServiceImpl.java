package com.greyhound.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.greyhound.constant.AppConstant;
import com.greyhound.constant.EmailConstant;
import com.greyhound.model.User;
import com.greyhound.service.IEmailService;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class EmailServiceImpl implements IEmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private Environment environment;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendEmail(String toEmail, String subject, String body, String label, List<String> attachmentUrlList,
			List<String> ccEmailList) {
		new Thread(() -> {
			try {
				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
				helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(environment.getProperty(AppConstant.SUPPORT_EMAIL));// add mail address
				helper.setTo(toEmail);
				helper.setSubject(subject);
				mimeMessage.setFrom(new InternetAddress(environment.getProperty(AppConstant.SUPPORT_EMAIL), label));
				mimeMessage.setContent(body, "text/html");

				if (ccEmailList != null && !ccEmailList.isEmpty()) {
					for (String ccEmail : ccEmailList) {
						mimeMessage.addRecipients(Message.RecipientType.CC, ccEmail);
					}
				}
				mimeMessage.setFrom(new InternetAddress(environment.getProperty(AppConstant.SUPPORT_EMAIL), label));

				if (attachmentUrlList != null && !attachmentUrlList.isEmpty()) {
					for (String attachmentUrl : attachmentUrlList) {
						FileSystemResource file = new FileSystemResource(
								environment.getProperty(AppConstant.FILE_UPLOAD_DIR) + File.separator + attachmentUrl);
						helper.addAttachment(file.getFilename(), file);
					}
				}
				javaMailSender.send(mimeMessage);
				logger.info("Email Success!! Email sent to " + toEmail);
			} catch (MessagingException e) {
				logger.info("Email Failed!! MessagingException!! " + e.getMessage());
			} catch (UnsupportedEncodingException e) {
				logger.info("Email Failed!! UnsupportedEncodingException!! " + e.getMessage());
			}
		}).start();
	}

	@Override
	public String createEmailBodyForNewUser(User user, String password) {
		String htmlString = EmailConstant.EMAIL_BODY_FOR_NEW_USER;
		htmlString = htmlString.replace("%name%", user.getName());
		htmlString = htmlString.replace("%username%", user.getEmail());
		htmlString = htmlString.replace("%password%", password);
		return htmlString;
	}

	@Override
	public String createEmailBodyForChangePassword(String name) {
		String htmlString = EmailConstant.EMAIL_BODY_FOR_CHANGE_PASSWORD;
		htmlString = htmlString.replace("%name%", name);
		return htmlString;
	}

	@Override
	public String createEmailBodyForForgotPassword(String name, String password) {
		String htmlString = EmailConstant.EMAIL_BODY_FOR_FORGOT_PASSWORD;
		htmlString = htmlString.replace("%name%", name);
		htmlString = htmlString.replace("%password%", password);
		return htmlString;
	}

	@Override
	public String createEmailBodyForOtp(String name, int otp) {
		String htmlString = EmailConstant.EMAIL_BODY_FOR_OTP;
		htmlString = htmlString.replace("%name%", name);
		htmlString = htmlString.replace("%otp%", String.valueOf(otp));
		return htmlString;
	}

	@Override
	public String createEmailBodyForLogin(User user, Integer otp) {
		String htmlString = EmailConstant.EMAIL_BODY_FOR_LOGIN_OTP;
		htmlString = htmlString.replace("%name%", user.getName());
		htmlString = htmlString.replace("%otp%", String.valueOf(otp));
		return htmlString;
	}
}
