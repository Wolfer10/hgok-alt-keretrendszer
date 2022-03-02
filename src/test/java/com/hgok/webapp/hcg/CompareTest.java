package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;


public class CompareTest {


    public static String COMPARED_DIR =  "src/main/resources/static/working-dir/x-compared";

   @Test
    public void testHCGCompareCreateFile() throws IOException {
        new ProcessHandler().startHCGCompare(AnalysisService.WORKINGPATH);
        Assertions.assertTrue(Path.of(COMPARED_DIR).toFile().exists());
        //Assertions.assertTrue(Path.of(COMPARED_DIR, "callgraph.json").toFile().exists());
    }





}
