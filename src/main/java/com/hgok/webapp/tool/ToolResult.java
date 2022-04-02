package com.hgok.webapp.tool;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.compared.MetricContainer;
import com.hgok.webapp.hcg.ProcessHandler;
import com.hgok.webapp.util.FileHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ToolResult {

    private static final Logger logger = LoggerFactory.getLogger(ToolResult.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Transient
    byte[] result;

    @ManyToOne(fetch = FetchType.LAZY)
    Analysis analysis;

    @OneToOne
    Tool tool;

    @OneToOne
    private MemoryData memoryData;

    private Long validationLength;

    public ToolResult(Tool tool, List<Path> list, Analysis analysis) throws IOException, ExecutionException, InterruptedException {
        initResult(tool.generateTokensFromFilePaths(list));
        this.tool = tool;
        this.analysis = analysis;
    }

    public ToolResult(Tool tool, Path path, Analysis analysis) throws IOException, ExecutionException, InterruptedException {
        initResult(tool.generateTokensFromFilePath(path));
        this.tool = tool;
        this.analysis = analysis;
    }


    public Process initResult(String... tokens) throws IOException, ExecutionException, InterruptedException {
        ProcessBuilder toolProcessBuilder = new ProcessBuilder(tokens);
        ProcessHandler processHandler = new ProcessHandler();
        //processHandler.setOs();
        Timestamp validationStart = new Timestamp(System.currentTimeMillis());
        logger.error("Tool futásának kezdete:");
        Process rawAnalysis = toolProcessBuilder.start();
        //memoryData = processHandler.calculateMemoryDataFromProcess(rawAnalysis).get();
        result = rawAnalysis.getInputStream().readAllBytes();
        logger.error(new String(rawAnalysis.getErrorStream().readAllBytes()));
        int exitValue = rawAnalysis.waitFor();
        Timestamp validationEnd = new Timestamp(System.currentTimeMillis());
        if(exitValue != 0){
            logger.error("Elromlott");
        } else {
            logger.info(Arrays.toString(tokens));
        }
        logger.info("Tool futásának Vége");


        validationLength = (validationEnd.getTime() - validationStart.getTime());

        return rawAnalysis;
    }

}
