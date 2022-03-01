package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class ConvertTest {

    @Test
    public void testJsonFileExits() throws IOException {

        String dirName = "ACG-NONE";
        String expectedFile = "TestFile2.json";
        Path path = Path.of(AnalysisService.WORKINGPATH, dirName);
        new ProcessHandler().startHCGConvert(path);

        Assertions.assertTrue(Path.of(AnalysisService.WORKINGPATH, dirName, expectedFile).toFile().exists());
    }




}
