package com.hgok.webapp.utilTests;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.util.JsonUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hgok.webapp.utilTests.FileHelperTest.TESTDIR;

public class JsonUtilTest {



    @Test
    public void testDumpToolNamesIntoJson() throws IOException {
        List<Tool> tools = new ArrayList<>(Arrays.asList(new Tool("alma"), new Tool("banan")));
        JsonUtil.dumpToolNamesIntoJson(tools, TESTDIR);
        Assert.assertTrue(Path.of(TESTDIR, "/tool-names.json").toFile().exists());

    }

    @Test
    public void test() throws IOException {
        ComparedAnalysis comparedTools = JsonUtil.getComparedToolsFromJson();
        Assert.assertNotNull(comparedTools);
        Assert.assertTrue(comparedTools.getLinks().size() > 0);
        System.out.println(comparedTools.getLinks());


    }
}
