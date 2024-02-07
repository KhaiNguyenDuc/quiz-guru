package com.khai.quizguru.config;

import com.khai.quizguru.security.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Custom implementation of AuditorAware interface to provide the current auditor for entity auditing.
 */
public class CustomAuditAware implements AuditorAware<String> {

    /**
     * Retrieves the current auditor (user ID) for entity auditing.
     * @return Optional containing the current auditor's user ID, or empty if no authenticated user is found
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}
