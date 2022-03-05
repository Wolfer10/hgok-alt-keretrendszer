package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;


public class CompareTest {


    public static String COMPARED_DIR =  "src/test/java/com/hgok/webapp/hcg/testdir/x-compared";

   @Test
   @Deprecated
    public void testHCGCompareCreateFile() throws IOException {
        new ProcessHandler().startHCGCompare(ConvertTest.HCG_TESTDIR);
        Assertions.assertTrue(Path.of(COMPARED_DIR).toFile().exists());
        Assertions.assertTrue(Path.of(COMPARED_DIR, "callgraph.json").toFile().exists());
    }





}
