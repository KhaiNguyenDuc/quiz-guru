package com.khai.quizguru.repository;

import com.khai.quizguru.model.RefreshToken;
import com.khai.quizguru.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String requestRefreshToken);

    Optional<RefreshToken> findByUser(User user);
}
