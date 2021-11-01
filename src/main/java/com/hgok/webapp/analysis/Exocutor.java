package com.hgok.webapp.analysis;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exocutor {

    private static ExecutorService executor  = Executors.newSingleThreadExecutor();
    private static ListeningExecutorService lExecService = MoreExecutors.listeningDecorator(executor);


    public static void submit(Runnable runnable){
        executor.submit(runnable);
    }


}
