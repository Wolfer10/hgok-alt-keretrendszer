package com.hgok.webapp.tool;

import com.hgok.webapp.util.FileHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ToolResultTest {

    public static final String TOOL_DIR = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\tool";
    public static final String[] TOKENS_RELATIVE = "node F:\\Feri\\egyetem\\szakdoga\\hcg-js-framework\\util\\js-callgraph\\js-callgraph.js --strategy NONE --cg src\\test\\java\\com\\hgok\\webapp\\util\\TestFile.js".split(" ");

    ToolResult result;

    @BeforeEach
    public void init(){
        result = new ToolResult();
    }

    @Test
    void initResult() {
    }



    @Test
    void appendResultToFile() throws IOException {
        String expected = "alma";
        result.setResult((expected + "\n").getBytes());
        FileHelper fileHelper = new FileHelper();
        fileHelper.removeDirByName(Path.of(TOOL_DIR).toString(), expected);
        Path expectedPath = fileHelper.createDirAndInsertFile(Path.of(TOOL_DIR), expected);
        String expectedFileName = expected + ".cgtxt";
        assertThat(expectedPath).exists().isEmptyFile().hasFileName(expectedFileName);
        result.appendResultToFile(expectedPath);
        result.appendResultToFile(expectedPath);
        assertThat(expectedPath).exists().isNotEmptyFile();
        assertThat(Files.readAllLines(expectedPath)).hasSize(2);
        assertThat(Files.readAllLines(expectedPath).get(0)).isEqualTo(expected);
        assertThat(Files.readAllLines(expectedPath).get(1)).isEqualTo(expected);
    }


    @Test
    public void testGetToolsResult() throws IOException {
        String[] tokens = "node F:\\Feri\\egyetem\\szakdoga\\hcg-js-framework\\util\\js-callgraph\\js-callgraph.js --strategy NONE --cg F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\TestFile.js".split(" ");
        //Path path = Path.of(analysisService.WORKINGPATH, dirName);
        result.initResult(tokens);
        Assertions.assertTrue(result.getResult().length > 0);
        //Assert.assertTrue(Path.of(analysisService.WORKINGPATH, dirName, "callgraph.json").toFile().exists());
    }

    @Test
    public void testWithRelativePath() throws IOException {
        //Path path = Path.of(analysisService.WORKINGPATH, dirName);
        result.initResult(TOKENS_RELATIVE);
        Assertions.assertTrue(result.getResult().length > 0);
        //Assert.assertTrue(Path.of(analysisService.WORKINGPATH, dirName, "callgraph.json").toFile().exists());
    }

}