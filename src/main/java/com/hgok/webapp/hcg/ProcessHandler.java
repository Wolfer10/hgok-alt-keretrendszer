package com.hgok.webapp.hcg;

import com.hgok.webapp.tool.MemoryData;
import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<MemoryData> calculateMemoryDataFromProcess(Process process) {
        CompletableFuture<MemoryData> completableFuture = new CompletableFuture<>();
        MemoryData memoryData = new MemoryData();
        long maxMemory = 0;
        long sumMemory = 0;
        long count = 1;
        while (isRunning(process)) {
            sumMemory += memoryUtilizationPerProcess(process.pid());
            maxMemory = Math.max(maxMemory, memoryUtilizationPerProcess(process.pid()));
            ++count;
        }
        memoryData.setMaxMemory(maxMemory);
        memoryData.setAverageMemory(sumMemory/count);
        completableFuture.complete(memoryData);
        return completableFuture;
    }

    public long memoryUtilizationPerProcess(long pid) {
        OSProcess osProcess = os.getProcess((int) pid);
        if (osProcess == null){
           return 0;
        }
        return osProcess.getResidentSetSize();

    }

    boolean isRunning(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }


}
