package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.TokenRefreshException;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.model.user.VerificationToken;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.repository.VerificationTokenRepository;
import com.khai.quizguru.service.VerifyTokenService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

/**
 * Service implementation for verifying user accounts using verification tokens.
 * Provides methods to find verification tokens by user and to verify user accounts using tokens.
 */
@Service
@RequiredArgsConstructor
public class VerifyTokenServiceImpl implements VerifyTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    /**
     * Finds the verification token associated with the specified user.
     *
     * @param userId The ID of the user.
     * @return The verification token associated with the user, or null if not found.
     * @throws InvalidRequestException If the user is not found in the database.
     */
    @Override
    public VerificationToken findTokenByUser(String userId) {

        Optional<User> userOpt= userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

        User user = userOpt.get();

        Optional<VerificationToken> tokenOpt = verificationTokenRepository.findByUser(user);
        return tokenOpt.orElse(null);

    }

    /**
     * Verifies the user's account by comparing the provided token with the one associated with the user.
     * If the token is valid and not expired, enables the user's account.
     *
     * @param token    The verification token provided by the user.
     * @param username The username or email of the user.
     * @return True if the user account is successfully verified, false otherwise.
     * @throws InvalidRequestException  If the user is not found in the database.
     * @throws TokenRefreshException    If the verification token is expired or invalid.
     */
    @Override
    public Boolean verifyUser(String token, String username) {
        Optional<User> userOpt;
        if(username.contains("@")){
            userOpt = userRepository.findByEmail(username);
        }else{
            userOpt = userRepository.findByUsername(username);
        }

        if(userOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

        User user = userOpt.get();

        Optional<VerificationToken> tokenOpt = verificationTokenRepository.findByUser(user);
        if(tokenOpt.isEmpty()){
            throw new TokenRefreshException(Constant.TOKEN_VERIFY_EXPIRED_MSG);
        }
        VerificationToken verificationToken = tokenOpt.get();
        if (verificationToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            verificationTokenRepository.delete(verificationToken);
            throw new TokenRefreshException(Constant.TOKEN_VERIFY_EXPIRED_MSG);
        }

        if(verificationToken.getToken().equals(token)){

            user.setIsEnable(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
