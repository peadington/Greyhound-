package com.greyhound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greyhound.model.Trap;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface TrapRepository extends JpaRepository<Trap, Long> {

	List<Trap> findByGreyhoundId(long greyhoundId);

}
