package com.hgok.webapp.tool;

import com.hgok.webapp.util.FileHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Path;

@Getter
@Setter
@NoArgsConstructor
public class ToolResult {

    byte[] result;

    public ToolResult(String... tokens) throws IOException {
        initResult(tokens);
    }

    public void initResult(String... tokens) throws IOException {
        ProcessBuilder toolProcessBuilder = new ProcessBuilder(tokens);
        Process rawAnalysis = toolProcessBuilder.start();
        //System.err.println( new String(rawAnalysis.getErrorStream().readAllBytes()));
        //System.out.println( new String(rawAnalysis.getInputStream().readAllBytes()));
        result = rawAnalysis.getInputStream().readAllBytes();
    }

    public void appendResultToFile(Path fullPath) throws IOException {
        FileHelper fileHelper = new FileHelper();
        fileHelper.appendToFile(fullPath, result);
    }


}
