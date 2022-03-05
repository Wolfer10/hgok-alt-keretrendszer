package com.hgok.webapp.hcg;

import com.hgok.webapp.analysis.AnalysisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class ConvertTest {
    public static final String HCG_TESTDIR = "src/test/java/com/hgok/webapp/hcg/testdir/";

    @Test
    public void testJsonFileExits() throws IOException {
        String dirName = "TEST-TOOL";
        String expectedFile = "alma" + ".json";
        String expectedPath = HCG_TESTDIR;
        Path path = Path.of(expectedPath, dirName);
        new ProcessHandler().startHCGConvert(path);
        Assertions.assertTrue(Path.of(expectedPath, dirName, expectedFile).toFile().exists());
    }




}
