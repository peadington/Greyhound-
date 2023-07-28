package com.greyhound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.greyhound.model.Greyhound;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface GreyhoundRepository extends JpaRepository<Greyhound, Long> {

	List<Greyhound> findByNameContainingIgnoreCase(String search);

	List<Greyhound> findByTrackContainingIgnoreCaseAndNameContainingIgnoreCase(String track, String name);

	List<Greyhound> findByTrackContainingIgnoreCase(String track);

	List<Greyhound> findByTrainerIdIn(List<Long> trainerIdList);

	Greyhound findByGreyhoundId(Long greyhoundId);

	List<Greyhound> findByTrainerId(long trainer);

	@Query(value = "SELECT DISTINCT gd.greyhound_id FROM greyhound gd where gd.trainer_id = ?1", nativeQuery = true)
	List<Long> findDistinctGrehoundIdsByTrainer(long trainerId);

	List<Greyhound> findByGreyhoundIdIn(List<Long> greyhoundIds);

	boolean existsByGreyhoundIdAndTrack(Long dogId, String track);

	List<Greyhound> findByTrackInAndTrainerId(List<String> tracks, long trainer);

	List<Greyhound> findByTrackAndTrainerIdIn(String track, List<Long> trainerList);

}
