package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.exception.TokenRefreshException;
import com.khai.quizguru.model.RefreshToken;
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

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.refreshTokenDurationMs}")
    private Long refreshTokenDurationMs;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
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

    @Override
    public RefreshToken findByToken(String requestRefreshToken) {

        Optional<RefreshToken> refreshTokenOtp = refreshTokenRepository.findByToken(requestRefreshToken);
        if(refreshTokenOtp.isPresent()){
            return refreshTokenOtp.get();
        }else{
            throw new TokenRefreshException(Constant.TOKEN_INVALID_MSG);
        }
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
                throw new TokenRefreshException(Constant.TOKEN_REFRESH_EXPIRED_MSG);
        }

        return token;
    }

}
