package com.hgok.webapp.analysis;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Exocutor {

    public static ExecutorService executor  = Executors.newSingleThreadExecutor();

    public static Future<?> addToQueue(Runnable runnable){
        return executor.submit(runnable);
    }


}
