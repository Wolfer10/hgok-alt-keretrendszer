package com.hgok.webapp.analysis;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Exocutor {

    private static ExecutorService executor  = Executors.newSingleThreadExecutor();
    private static ListeningExecutorService lExecService = MoreExecutors.listeningDecorator(executor);



    public static void addToQueue(Runnable runnable){
        executor.submit(runnable);


    }


}
