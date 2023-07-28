package com.greyhound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greyhound.model.Trainer;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

	List<Trainer> findByNameContainingIgnoreCase(String keyword);

	Trainer findByName(String trainerName);

	boolean existsByName(String name);

}
