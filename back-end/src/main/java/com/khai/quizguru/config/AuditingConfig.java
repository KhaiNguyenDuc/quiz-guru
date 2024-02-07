package com.khai.quizguru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class for enabling JPA auditing and providing the auditor provider.
 */
@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    /**
     * Bean definition for the auditor provider.
     * @return CustomAuditAware instance as the auditor provider
     */
    @Bean
    AuditorAware<String> auditorProvider() {
        return new CustomAuditAware();
    }
}