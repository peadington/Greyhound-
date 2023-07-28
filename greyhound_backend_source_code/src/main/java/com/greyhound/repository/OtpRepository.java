package com.greyhound.repository;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greyhound.model.Otp;

/**
 * 
 * @author p4logics
 *
 */
@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

	Otp findFirstByEmailAndOtpOrderByIdDesc(@Valid String email, @Valid Integer otp);

	Otp findFirstByTokenAndOtpOrderByIdDesc(@Valid String token, @Valid int otp);

	Otp findByTokenAndOtp(String token, int otp);

}
