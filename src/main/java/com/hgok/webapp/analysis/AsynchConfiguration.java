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
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor()
    {
        return Executors.newSingleThreadExecutor();
    }
}
