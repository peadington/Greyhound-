package com.greyhound.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.model.Track;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface ITrackService {

	void getAllTrack(ApiResponseDtoBuilder apiResponseDtoBuilder);

	List<Track> findAll();

}
