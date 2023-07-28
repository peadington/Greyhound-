package com.greyhound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greyhound.model.GreyhoundProfile;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface GreyhoundProfileRepository extends JpaRepository<GreyhoundProfile, Long> {

	List<GreyhoundProfile> findByGreyhoundId(long greyhoundId);

	List<GreyhoundProfile> findByGreyhoundIdIn(List<Long> greyhoundIds);

	boolean existsByGreyhoundIdAndMeetingIdAndRaceId(Long dogId, Long meetingID, Long raceId);

}
