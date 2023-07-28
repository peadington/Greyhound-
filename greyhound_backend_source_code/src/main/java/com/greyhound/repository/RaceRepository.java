package com.greyhound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.greyhound.model.Race;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

	List<Race> findByRaceId(Long raceId);

	@Query(value = "SELECT DISTINCT r.race_class FROM race r", nativeQuery = true)
	List<String> findDistinctGrade();

	@Query(value = "SELECT DISTINCT r.distance FROM race r", nativeQuery = true)
	List<Integer> findDistinctDistance();

}
