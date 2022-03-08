package com.hgok.webapp.tool;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.hcg.ProcessHandler;
import com.hgok.webapp.util.FileHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

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

    private Long validationLength;

    public ToolResult(Tool tool, List<Path> list, Analysis analysis) throws IOException {
        initResult(tool.generateTokensFromFilePaths(list));
        this.tool = tool;
        this.analysis = analysis;
    }

    public Process initResult(String... tokens) throws IOException {
        ProcessBuilder toolProcessBuilder = new ProcessBuilder(tokens);
        ProcessHandler processHandler = new ProcessHandler();
        processHandler.setOs();
        Timestamp validationStart = new Timestamp(System.currentTimeMillis());
        Process rawAnalysis = toolProcessBuilder.start();
        System.out.println(processHandler.memoryUtilizationPerProcess(rawAnalysis.pid()));
        Timestamp validationEnd = new Timestamp(System.currentTimeMillis());
        result = rawAnalysis.getInputStream().readAllBytes();
        validationLength = (validationEnd.getTime() - validationStart.getTime());
        return rawAnalysis;
    }

    public void appendResultToFile(Path fullPath) throws IOException {
        FileHelper fileHelper = new FileHelper();
        fileHelper.appendToFile(fullPath, result);
    }


}
