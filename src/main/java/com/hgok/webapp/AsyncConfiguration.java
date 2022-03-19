package com.hgok.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;


@Configuration
@EnableAsync
public class AsyncConfiguration
{
    @Bean(name = "single-thread")
    public Executor singleThreadAsync()
    {
        return Executors.newSingleThreadExecutor();
    }

    @Bean(name = "async-multithread")
    public Executor multiThreadAsync()
    {
        return Executors.newSingleThreadExecutor();
    }
}
