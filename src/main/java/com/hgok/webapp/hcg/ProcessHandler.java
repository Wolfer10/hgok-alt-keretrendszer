package com.hgok.webapp.hcg;

import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.nio.file.Path;

public class ProcessHandler {


    public void startHCGConvert(Path dir) throws IOException {
        ProcessBuilder convertProcessBuilder = new ProcessBuilder("python", "src/main/resources/hcg/jscg_convert2json.py", dir.toString());
        Process convertProcess = convertProcessBuilder.start();
        String convertResult = new String(convertProcess.getInputStream().readAllBytes());
        String error = new String(convertProcess.getErrorStream().readAllBytes());
//        System.err.println(error);
//        System.out.println(convertResult);
    }

    public void startHCGCompare(String dir) throws IOException {
        //todo LOGs
        ProcessBuilder convertProcessBuilder = new ProcessBuilder("python", "src/main/resources/hcg/jscg_compare_json.py", dir, "noentry", "nowrapper");
        Process convertProcess = convertProcessBuilder.start();
        String result = new String(convertProcess.getInputStream().readAllBytes());
        String error = new String(convertProcess.getErrorStream().readAllBytes());

//        System.err.println(error);
//        System.out.println(result);
    }

}
