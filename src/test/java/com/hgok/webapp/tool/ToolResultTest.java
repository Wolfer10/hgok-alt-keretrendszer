package com.hgok.webapp.tool;

import com.hgok.webapp.util.FileHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ToolResultTest {

    public static final String TOOL_DIR = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\tool";

    @Test
    void initResult() {
    }

    @Test
    void appendResultToFile() throws IOException {
        ToolResult toolResult = new ToolResult();
        String expected = "alma";
        toolResult.setResult(expected.getBytes());
        FileHelper fileHelper = new FileHelper();
        Path expectedPath = fileHelper.createDirAndInsertFile(Path.of(TOOL_DIR), expected);
        String expectedFileName = expected + ".cgtxt";
        assertThat(expectedPath).exists().isEmptyFile().hasFileName(expectedFileName);
        toolResult.appendResultToFile(expectedPath);
        assertThat(expectedPath).exists().isNotEmptyFile();
        assertThat(Files.readAllLines(expectedPath)).hasSize(1);
        assertThat(Files.readAllLines(expectedPath).get(0)).isEqualTo(expected);
    }
}