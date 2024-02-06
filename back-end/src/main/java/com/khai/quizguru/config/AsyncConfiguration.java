package com.khai.quizguru.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

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