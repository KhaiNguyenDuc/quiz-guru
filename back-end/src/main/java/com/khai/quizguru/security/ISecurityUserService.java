package com.khai.quizguru.security;

/**
 * Interface for loading user details by user ID.
 */
public interface ISecurityUserService {

    /**
     * Load user details by user ID.
     *
     * @param userId The ID of the user to load.
     * @return UserPrincipal object representing the user details.
     */
    UserPrincipal loadUserById(String userId);
}
