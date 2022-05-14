package com.hgok.webapp.hcg;


import com.hgok.webapp.tool.MemoryData;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ProcessHandler.class);

    public ProcessHandler() {
        setOs();
    }

    public void startHCGConvert(Path dir) throws IOException, InterruptedException {
        logger.error("HCG CONVERT STARTED");
        ProcessBuilder convertProcessBuilder = new ProcessBuilder("python", "src/main/resources/hcg/jscg_convert2json.py", dir.toString());
        Process convertProcess = convertProcessBuilder.start();
        logger.info(new String(convertProcess.getInputStream().readAllBytes()));
        logger.info(new String((convertProcess.getErrorStream().readAllBytes())));
        logger.error("CONVERT ENDED: " + convertProcess.waitFor());
    }

    public void startHCGCompare(String dir) throws IOException, InterruptedException {
        logger.error("HCG COMPARE STARTED");
        ProcessBuilder convertProcessBuilder = new ProcessBuilder("python", "src/main/resources/hcg/jscg_compare_json.py", dir, "noentry", "nowrapper");
        Process convertProcess = convertProcessBuilder.start();
        logger.info(new String(convertProcess.getInputStream().readAllBytes()));
        logger.info(new String((convertProcess.getErrorStream().readAllBytes())));
        logger.error("COMPARE ENDED: " + convertProcess.waitFor());
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
