package com.khai.quizguru.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration class for asynchronous task execution.
 */
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    /**
     * Configures and provides an executor for asynchronous task execution.
     * @return Executor for asynchronous task execution
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set your desired core pool size
        executor.setMaxPoolSize(10); // Set your desired max pool size
        executor.setQueueCapacity(25); // Set your desired queue capacity
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}