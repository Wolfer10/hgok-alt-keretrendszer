package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class ToolProcessTest {


    @Test
    public void getToolsResult() throws IOException {
        AnalysisService analysisService = new AnalysisService();
        String[] tokens = "node F:\\Feri\\egyetem\\szakdoga\\hcg-js-framework\\util\\js-callgraph\\js-callgraph.js --strategy NONE --cg F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\utilTests\\TestFile.js".split(" ");
        //Path path = Path.of(analysisService.WORKINGPATH, dirName);
        Assert.assertTrue(analysisService.getToolsResult(tokens).length > 0);
        //Assert.assertTrue(Path.of(analysisService.WORKINGPATH, dirName, "callgraph.json").toFile().exists());
    }
}
