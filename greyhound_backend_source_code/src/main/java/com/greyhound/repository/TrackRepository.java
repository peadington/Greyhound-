package com.greyhound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greyhound.model.Track;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

}
