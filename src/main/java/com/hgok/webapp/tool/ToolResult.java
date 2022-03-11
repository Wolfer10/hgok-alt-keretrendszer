package com.hgok.webapp.tool;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.hcg.ProcessHandler;
import com.hgok.webapp.util.FileHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ToolResult {


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
        processHandler.setOs();
        Timestamp validationStart = new Timestamp(System.currentTimeMillis());
        Process rawAnalysis = toolProcessBuilder.start();
        Timestamp validationEnd = new Timestamp(System.currentTimeMillis());

        //memoryData = processHandler.calculateMemoryDataFromProcess(rawAnalysis).get();
        result = rawAnalysis.getInputStream().readAllBytes();
        validationLength = (validationEnd.getTime() - validationStart.getTime());

        return rawAnalysis;
    }

    public void appendResultToFile(Path fullPath) throws IOException {
        FileHelper fileHelper = new FileHelper();
        fileHelper.appendToFile(fullPath, result);
    }


}
