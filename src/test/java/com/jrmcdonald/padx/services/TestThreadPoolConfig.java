package com.jrmcdonald.padx.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ThreadPoolConfig
 */
@Configuration
public class TestThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor testTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();

        return executor;
    }
   
}