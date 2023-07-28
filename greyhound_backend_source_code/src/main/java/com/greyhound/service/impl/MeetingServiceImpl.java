package com.greyhound.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyhound.dto.MeetingDto;
import com.greyhound.model.Meeting;
import com.greyhound.repository.MeetingRepository;
import com.greyhound.service.IMeetingService;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class MeetingServiceImpl implements IMeetingService {

	static Logger logger = Logger.getLogger(MeetingServiceImpl.class);

	@Autowired
	private MeetingRepository meetingRepository;

	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public void addMeetingDetails(MeetingDto meetingDto) {
		Meeting meeting = new Meeting();
		try {
			meeting.setDate(new java.sql.Date(format.parse(meetingDto.getRaceDate()).getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		meeting.setMeetingId(meetingDto.getMeetingId());
		meeting.setRaceId(meetingDto.getRaceId());
		meeting.setTrack(meetingDto.getTrackName());
		meetingRepository.save(meeting);
		logger.info("Data Insert Successfully in meeting table meetingID:-" + meeting.getMeetingId());
	}
}
