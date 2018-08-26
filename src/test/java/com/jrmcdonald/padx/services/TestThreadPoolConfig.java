package com.jrmcdonald.padx.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Test Thread Pool Config
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@Configuration
public class TestThreadPoolConfig {

    /**
     * Bean to configure a thread pool task executor for tests.
     */
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