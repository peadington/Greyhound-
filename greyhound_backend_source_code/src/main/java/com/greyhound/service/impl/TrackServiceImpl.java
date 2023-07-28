package com.greyhound.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.model.Track;
import com.greyhound.repository.TrackRepository;
import com.greyhound.service.ITrackService;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class TrackServiceImpl implements ITrackService {

	@Autowired
	private TrackRepository trackRepository;

	@Override
	public void getAllTrack(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Track> trackList = findAll();
		if (!trackList.isEmpty())
			apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage("Track List").withData(trackList);
		else
			apiResponseDtoBuilder.withStatus(HttpStatus.NOT_FOUND).withMessage("Tracks not found");
	}

	@Override
	public List<Track> findAll() {
		return trackRepository.findAll();
	}

}
