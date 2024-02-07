package com.khai.quizguru.security;

import com.khai.quizguru.exception.AccessDeniedException;
import com.khai.quizguru.exception.UnauthorizedException;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService, ISecurityUserService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user;
        if(username.contains("@")){
            user = userRepository.findByEmail(username);
        }else{
            user = userRepository.findByUsername(username);
        }
        if(user.isPresent()){
            if(!user.get().getIsEnable()){
                throw new AccessDeniedException(Constant.ACCESS_DENIED_MSG);
            }
            return UserPrincipal.create(user.get());
        }else{
            throw new UnauthorizedException(Constant.UNAUTHORIZED_MSG);
        }

    }

    @Override
    public UserPrincipal loadUserById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return UserPrincipal.create(user.get());
        }else{
            throw new UnauthorizedException(Constant.UNAUTHORIZED_MSG);
        }

    }
}
