package com.hgok.webapp.analysis;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AnalysisService {


    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);


    void startProcess(String command, String name) {
        String[] tokens = command.split(" ");
        Exocutor.submit(() -> {
            try {
                log.error(name + " kezdete");
                new ProcessBuilder(tokens).start();
                Thread.sleep(6000);
                log.error(name + "vége");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }



}
