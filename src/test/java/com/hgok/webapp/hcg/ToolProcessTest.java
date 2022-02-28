package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.util.FileHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

public class ToolProcessTest {


    public static final String[] TOKENS_RELATIVE = "node F:\\Feri\\egyetem\\szakdoga\\hcg-js-framework\\util\\js-callgraph\\js-callgraph.js --strategy NONE --cg src\\test\\java\\com\\hgok\\webapp\\utilTests\\TestFile.js".split(" ");
    public static final String HCG_TESTDIR = "src/test/java/com/hgok/webapp/hcg/testdir/";

    @Test
    public void testGetToolsResult() throws IOException {
        AnalysisService analysisService = new AnalysisService();
        String[] tokens = "node F:\\Feri\\egyetem\\szakdoga\\hcg-js-framework\\util\\js-callgraph\\js-callgraph.js --strategy NONE --cg F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\utilTests\\TestFile.js".split(" ");
        //Path path = Path.of(analysisService.WORKINGPATH, dirName);
        Assertions.assertTrue(analysisService.getToolsResult(tokens).length > 0);
        //Assert.assertTrue(Path.of(analysisService.WORKINGPATH, dirName, "callgraph.json").toFile().exists());
    }

    @Test
    public void testWithRelativePath() throws IOException {
        AnalysisService analysisService = new AnalysisService();
        //Path path = Path.of(analysisService.WORKINGPATH, dirName);
        Assertions.assertTrue(analysisService.getToolsResult(TOKENS_RELATIVE).length > 0);
        //Assert.assertTrue(Path.of(analysisService.WORKINGPATH, dirName, "callgraph.json").toFile().exists());
    }

    @Test
    public void testWriteToolResultToNewFile() throws IOException {
        AnalysisService analysisService = new AnalysisService();
        Tool tool = new Tool();
        tool.setName("TEST-TOOL");
        byte[] expected = analysisService.getToolsResult(TOKENS_RELATIVE);
        Path path = analysisService.writeToolResultToDir(HCG_TESTDIR, tool, "alma", expected);
        Assertions.assertTrue(path.toFile().exists());
        Assertions.assertTrue(analysisService.getToolsResult(TOKENS_RELATIVE).length > 0);
        //AssertFromFile(path, expected);
    }

    @Test
    public void testWithParamtedTool() throws IOException {
        AnalysisService analysisService = new AnalysisService();
        Tool tool = new Tool();
        tool.setName("TEST-TOOL");
        tool.setLanguage("Javascript");
        tool.setArguments(String.format("--strategy NONE --cg %s", "src\\test\\java\\com\\hgok\\webapp\\utilTests\\TestFile.js"));
        tool.setPath("F:\\Feri\\egyetem\\szakdoga\\hcg-js-framework\\util\\js-callgraph\\js-callgraph.js");
        String[] tempTokens = new String[]{ tool.getCompilerNameFromTool(), tool.getPath(), };

        String[] both = Stream.concat(Arrays.stream(tempTokens), Arrays.stream(tool.getArguments().split(" ")))
                .toArray(String[]::new);

        byte[] expected = analysisService.getToolsResult(both);

        Path path = analysisService.writeToolResultToDir(HCG_TESTDIR, tool, "alma", expected);
        Assertions.assertTrue(path.toFile().exists());
        Assertions.assertTrue(expected.length > 0);
        //AssertFromFile(path, expected);
    }

}
