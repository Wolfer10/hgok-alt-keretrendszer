package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;


class ComparedAnalysisTest {

    private ComparedAnalysis comparedAnalysis;
    private static final String TEST_JSON = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\compared\\TestFile.json";
    private static final String EXPECTED = "F:/Feri/egyetem/szakdoga/hgok-alt-keretrendszer/src/main/resources/static/working-dir/sourceFiles/TestFile.js:10:1->F:/Feri/egyetem/szakdoga/hgok-alt-keretrendszer/src/main/resources/static/working-dir/sourceFiles/TestFile.js:5:1";


    @BeforeEach
    public void init(){
        comparedAnalysis = new ComparedAnalysis();
    }

    @Test
    void initComparedAnalysis() {
        Analysis analysis = new Analysis();
        ComparedAnalysis comparedAnalysis2 = ComparedAnalysis.initComparedAnalysis(Path.of(TEST_JSON), analysis);
        assertThat(comparedAnalysis2.getAnalysis()).isEqualTo(analysis);
        assertThat(comparedAnalysis2.getLinks()).hasSize(1);
        assertThat(comparedAnalysis2.getLinks().get(0).getLabel()).isEqualTo(EXPECTED);
        assertThat(comparedAnalysis2.getFileName()).isEqualTo("TestFile.json");
    }

    @Test
    void setLinkSourceAndTarget() {
    }

    @Test
    void getToolMetrics() {
    }

    @Test
    void initMetricContainer() {
    }

    @Test
    void updateMetricContainers() {
    }
}