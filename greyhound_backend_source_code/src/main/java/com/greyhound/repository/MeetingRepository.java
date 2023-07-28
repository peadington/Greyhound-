package com.greyhound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greyhound.model.Meeting;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
