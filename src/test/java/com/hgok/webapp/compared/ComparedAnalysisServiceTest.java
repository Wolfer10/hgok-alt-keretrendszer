package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.util.ListHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ComparedAnalysisServiceTest {

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

        assertThat(ListHelper.flatMap(analysis
                .getComparedAnalysises()
                .stream()
                .map(ComparedAnalysis::getMetricContainers))).isNotEmpty().hasSize(3);

        for (ComparedAnalysis comparedAnalysise : analysis.getComparedAnalysises()) {
                assertThat(comparedAnalysise.getMetricContainers()).isNotNull();
        }
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
        analysis.setComparedAnalysises(List.of(comparedAnalysis));
        return analysis;
    }
}
