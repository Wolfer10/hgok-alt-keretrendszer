package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class ConvertTest {

    @Test
    public void testJsonFileExits() throws IOException {
        AnalysisService analysisService = new AnalysisService();
        String dirName = "test NONE";
        Path path = Path.of(AnalysisService.WORKINGPATH, dirName);
        analysisService.startHCGConvert(path);
        Assertions.assertTrue(Path.of(AnalysisService.WORKINGPATH, dirName, "callgraph.json").toFile().exists());
    }




}
