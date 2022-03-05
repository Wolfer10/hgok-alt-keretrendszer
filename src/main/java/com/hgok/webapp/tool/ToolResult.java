package com.hgok.webapp.tool;

import com.hgok.webapp.util.FileHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "id", nullable = false)
    private Long id;

    @Transient
    byte[] result;

    @ManyToOne(fetch = FetchType.LAZY)
    Tool tool;

    private Time validationLength;

    public ToolResult(Tool tool, List<Path> list) throws IOException {
        this.tool = tool;
        initResult(tool.generateTokensFromFilePaths(list));
    }

    public void initResult(String... tokens) throws IOException {
        ProcessBuilder toolProcessBuilder = new ProcessBuilder(tokens);
        Timestamp validationStart = new Timestamp(System.currentTimeMillis());
        Process rawAnalysis = toolProcessBuilder.start();
        Timestamp validationEnd = new Timestamp(System.currentTimeMillis());
        //System.err.println( new String(rawAnalysis.getErrorStream().readAllBytes()));
        //System.out.println( new String(rawAnalysis.getInputStream().readAllBytes()));
        result = rawAnalysis.getInputStream().readAllBytes();
        validationLength = new Time(validationEnd.getTime() - validationStart.getTime());
    }

    public void appendResultToFile(Path fullPath) throws IOException {
        FileHelper fileHelper = new FileHelper();
        fileHelper.appendToFile(fullPath, result);
    }


}
