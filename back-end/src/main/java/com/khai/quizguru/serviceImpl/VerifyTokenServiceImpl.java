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

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifyTokenServiceImpl implements VerifyTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
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
