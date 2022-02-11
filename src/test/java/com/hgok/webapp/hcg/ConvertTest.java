package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void test() throws IOException {
        AnalysisService analysisService = new AnalysisService();
        String dirName = "test NONE";
        Path path = Path.of(analysisService.WORKINGPATH, dirName);
        analysisService.startHCGConvert(path);
        Assert.assertTrue(Path.of(analysisService.WORKINGPATH, dirName, "callgraph.json").toFile().exists());
    }




}
