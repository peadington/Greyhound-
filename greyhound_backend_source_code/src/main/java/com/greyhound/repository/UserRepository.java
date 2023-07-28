package com.greyhound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greyhound.model.User;
import com.greyhound.repository.custom.UserRepositoryCustom;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

	User findByEmail(String username);

	boolean existsByEmail(String email);

	User findByEmailIgnoreCase(String currentUsername);

	User findFirstByOrderByIdDesc();

	List<User> findByRole(int role);

	List<User> findByRoleNot(int role);

}
