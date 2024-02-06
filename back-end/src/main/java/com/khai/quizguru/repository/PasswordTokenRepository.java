package com.khai.quizguru.repository;

import com.khai.quizguru.model.user.PasswordResetToken;
import com.khai.quizguru.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByUser(User user);
}
