package com.greyhound.service;

import org.springframework.stereotype.Service;

import com.greyhound.dto.MeetingDto;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IMeetingService {

	void addMeetingDetails(MeetingDto meetingDto);

}
