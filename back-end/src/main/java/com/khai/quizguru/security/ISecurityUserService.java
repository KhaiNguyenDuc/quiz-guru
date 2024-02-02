package com.khai.quizguru.security;

public interface ISecurityUserService {
    UserPrincipal loadUserById(String userId);
}
