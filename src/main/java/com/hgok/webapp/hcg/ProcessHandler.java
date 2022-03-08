package com.hgok.webapp.hcg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Async;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ProcessHandler {

    @Getter
    OperatingSystem os;

    public ProcessHandler() {
    }

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

    public void setOs() {
        SystemInfo si = new SystemInfo();
        os = si.getOperatingSystem();
    }

    @Async("async-multithread")
    public CompletableFuture<Long> maxMemory(Process process){
        long maxMemory = 0;
        while (process.isAlive()){
         maxMemory  = Math.max(maxMemory, memoryUtilizationPerProcess(process.pid()));
        }
        return CompletableFuture.completedFuture(maxMemory);
    }

    public long memoryUtilizationPerProcess(long pid) {
        System.out.println(pid);
        System.out.println();
        System.out.println(ProcessHandle.allProcesses().filter(processHandle -> processHandle.pid() ==  pid).collect(Collectors.toList()));
        return os.getProcess((int) pid).getResidentSetSize();
    }


}
