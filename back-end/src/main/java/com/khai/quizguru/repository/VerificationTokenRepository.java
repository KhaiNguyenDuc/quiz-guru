package com.khai.quizguru.repository;

import com.khai.quizguru.model.user.User;
import com.khai.quizguru.model.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

    Optional<VerificationToken> findByUser(User user);

}
