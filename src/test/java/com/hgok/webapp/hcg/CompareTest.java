package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.util.JsonUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CompareTest {


    public static String COMPARED_DIR =  "src/main/resources/static/working-dir/x-compared";

   @Test
    public void testHcgCompare() throws IOException {
        AnalysisService analysisService = new AnalysisService();
        analysisService.startHCGCompare(analysisService.WORKINGPATH);
        Assert.assertTrue(Path.of(COMPARED_DIR).toFile().exists());
        Assert.assertTrue(Path.of(COMPARED_DIR, "callgraph.json").toFile().exists());
    }





}
