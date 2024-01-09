package com.khai.quizguru.security;

import com.khai.quizguru.Exception.InvalidRequestException;
import com.khai.quizguru.Exception.ResourceNotFoundException;
import com.khai.quizguru.Exception.UnauthorizedException;
import com.khai.quizguru.model.User.User;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
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


        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
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
