package com.team2.resumeeditorproject.user.repository;

import com.team2.resumeeditorproject.user.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Verification findByEmail(String email);
}
