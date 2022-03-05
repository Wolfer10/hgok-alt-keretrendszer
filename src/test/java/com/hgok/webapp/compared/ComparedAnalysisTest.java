package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.tool.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
    public void updateMetrics(){

        ComparedAnalysis comparedAnalysis = new ComparedAnalysis();

        Analysis analysis = initAnalysis(comparedAnalysis);
        comparedAnalysis.setAnalysis(analysis);

        comparedAnalysis.updateMetricContainers(10);

        for (MetricContainer metricContainer : comparedAnalysis.getMetricContainers()) {
            assertThat(metricContainer.getOurTruePositive()).isEqualTo(10);
        }
    }

    @Test
    public void setMetrics(){
        ComparedAnalysis comparedAnalysis = new ComparedAnalysis();
        Analysis analysis = initAnalysis(comparedAnalysis);
        comparedAnalysis.setAnalysis(analysis);
        comparedAnalysis.addAllMetricContainer(new ArrayList<>(List.of(
                new MetricContainer(10,20,"alma"),
                new MetricContainer(10,30,"körte"),
                new MetricContainer(10,30,"barack"))));

        assertThat(analysis.getComparedAnalysis().getMetricContainers()).isNotEmpty().hasSize(3);
    }

    @Test
    public void testGetToolMetrics(){
        ComparedAnalysis comparedAnalysis = new ComparedAnalysis();
        Analysis analysis = initAnalysis(comparedAnalysis);
        comparedAnalysis.setAnalysis(analysis);
        List<MetricContainer> metricContainer = comparedAnalysis.getToolMetrics();
        assertThat(metricContainer).isNotEmpty().hasSize(1);
    }

    private Analysis initAnalysis(ComparedAnalysis comparedAnalysis) {
        Analysis analysis = new Analysis();
        analysis.setTools(new ArrayList<>(List.of(new Tool("TestTool"))));
        comparedAnalysis.setLinks(new ArrayList<>());
        analysis.setComparedAnalysis(comparedAnalysis);
        return analysis;
    }
}