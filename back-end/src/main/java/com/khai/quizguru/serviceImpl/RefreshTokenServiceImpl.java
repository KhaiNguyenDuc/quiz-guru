package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.exception.TokenRefreshException;
import com.khai.quizguru.model.user.RefreshToken;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.repository.RefreshTokenRepository;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.service.RefreshTokenService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the RefreshTokenService interface.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.refreshTokenDurationMs}")
    private Long refreshTokenDurationMs;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Creates a new refresh token for the specified user.
     *
     * @param userId The ID of the user for whom the refresh token is created.
     * @return The newly created refresh token.
     * @throws ResourceNotFoundException If the user with the specified ID is not found.
     */
    @Override
    public RefreshToken createRefreshToken(String userId) {

        Optional<User> userOtp = userRepository.findById(userId);
        if(userOtp.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        User user = userOtp.get();
        Optional<RefreshToken> refreshTokenOtp = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken = refreshTokenOtp.orElseGet(RefreshToken::new);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * Finds a refresh token by its token value.
     *
     * @param requestRefreshToken The token value to search for.
     * @return The refresh token if found.
     * @throws TokenRefreshException If the token is invalid.
     */
    @Override
    public RefreshToken findByToken(String requestRefreshToken) {

        Optional<RefreshToken> refreshTokenOtp = refreshTokenRepository.findByToken(requestRefreshToken);
        if(refreshTokenOtp.isPresent()){
            return refreshTokenOtp.get();
        }else{
            throw new TokenRefreshException(Constant.TOKEN_INVALID_MSG);
        }
    }

    /**
     * Verifies if a refresh token has expired.
     *
     * @param token The refresh token to verify.
     * @return The refresh token if it has not expired.
     * @throws TokenRefreshException If the token has expired.
     */
    public RefreshToken deleteIfExpired(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
                throw new TokenRefreshException(Constant.TOKEN_REFRESH_EXPIRED_MSG);
        }

        return token;
    }

}
