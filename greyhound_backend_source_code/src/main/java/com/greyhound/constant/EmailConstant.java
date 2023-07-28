package com.greyhound.constant;

/**
 * 
 * @author p4logics
 *
 */
public class EmailConstant {

	// Email Subject
	public static final String EMAIL_SUBJECT_FOR_NEW_USER = "Welcome to " + AppConstant.APP_NAME;
	public static final String EMAIL_SUBJECT_FOR_OTP = "Verification OTP";
	public static final String EMAIL_SUBJECT_FOR_FORGOT_PASSWORD = "Forgot password";
	public static final String EMAIL_SUBJECT_FOR_CHANGE_PASSWORD = "Change password";

	// Email Label
	public static final String EMAIL_LABEL_FOR_NEW_USER = AppConstant.APP_NAME;
	public static final String EMAIL_LABEL_FOR_OTP = AppConstant.APP_NAME;
	public static final String EMAIL_LABEL_FOR_FORGOT_PASSWORD = AppConstant.APP_NAME;
	public static final String EMAIL_LABEL_FOR_CHANGE_PASSWORD = AppConstant.APP_NAME;

	// Email body
	public static final String EMAIL_BODY_FOR_NEW_USER = "<!DOCTYPE html><html><head><title>Page Title</title></head><body><p>Hii, %name%</p><p>Welcome to "
			+ AppConstant.APP_NAME
			+ ". Please have a look on bellow credentials for login our portal:</p><p style=\"font-weight:700\">Username : %username%</p><p style=\"font-weight:700\">Password :%password%</p><br><p>Thanks & regards</p><p>"
			+ AppConstant.APP_NAME
			+ " Team</p><p style=\"color:red;text-align:center\">Note : Any issue for login our portal directly contact to admin.</p></body></html>";

	public static final String EMAIL_BODY_FOR_OTP = "<html><head></head><body><p>%name%,</p><p>Your verification code is :%otp%. This code is valid for 30 Minutes Please do not share this code with anyone.</p><p>Thanks & regards</p><p>"
			+ AppConstant.APP_NAME + " Team</p></body></html>";

	public static final String EMAIL_BODY_FOR_FORGOT_PASSWORD = "<!DOCTYPE html><html><head><body><p>%name%,</p><p>This is temporary password, We suggest you to change password after login into your account. </p><p> Your temporary password is : %password%</p>";

	public static final String EMAIL_BODY_FOR_CHANGE_PASSWORD = "<!DOCTYPE html><html><head><body><p>%name%,</p><p>Thanks & regards</p><p>"
			+ AppConstant.APP_NAME + " Team</p></body></html>";

	public static final String EMAIL_BODY_FOR_LOGIN_OTP = "<html><head></head><body><p>%name%,</p><p>Your verification code is :%otp%. This code is valid for 30 Minutes Please do not share this code with anyone.</p><p>Thanks & regards</p><p>"
			+ AppConstant.APP_NAME + " Team</p></body></html>";
}
