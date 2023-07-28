package com.greyhound.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.greyhound.constant.AppConstant;

/**
 * 
 * @author p4logics
 *
 */
public class CustomDateAndTimeDeserialize extends JsonDeserializer<Date> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			AppConstant.CUSTOM_DATE_AND_TIME_DESERIALIZE_DATE_FORMAT);

	@Override
	public Date deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
			throws IOException, JsonProcessingException {
		String str = paramJsonParser.getText().trim();
		if (str != null && !str.equals("")) {
			try {
				return dateFormat.parse(str);
			} catch (Exception e) {
				// Handle exception here
			}
			return paramDeserializationContext.parseDate(str);
		}
		return null;
	}
}