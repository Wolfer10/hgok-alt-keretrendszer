package com.hgok.webapp.analysis;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;


@Configuration
@EnableAsync
public class AsynchConfiguration
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
