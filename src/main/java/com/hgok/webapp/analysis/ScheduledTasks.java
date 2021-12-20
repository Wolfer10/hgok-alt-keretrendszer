package com.hgok.webapp.analysis;


import com.google.common.util.concurrent.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class ScheduledTasks {

    ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    ListenableFuture<String> asd = service.submit(new Callable<String>() {
        public String call() {
            return "ads";
        }
    });



}
