package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolResult;
import com.hgok.webapp.tool.ToolResultService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.NotBlank;
import java.sql.Time;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class ComparedAnalysisServiceTest {


    private ComparedAnalysisService comparedAnalysisService;

    @Mock
    private ToolResultService toolResultService;

    @Mock
    private ComparedAnalysis comparedAnalysis;


    @BeforeEach
    void setUp() {
        comparedAnalysisService = new ComparedAnalysisService(toolResultService, comparedAnalysis);
    }



    @Test
    void initToolResults() {
        ToolResult toolResult = new ToolResult();
        toolResult.setValidationLength(1000L);
        Analysis analysis = new Analysis();
        Tool tool = new Tool();
        tool.setId(1L);
        tool.setName("Alma");
        analysis.setTools(List.of(tool));
        MetricContainer metricContainer = new MetricContainer();
        metricContainer.setTool(tool);
        when(toolResultService.getToolResultByToolId(any(Long.class), any(Long.class))).thenReturn(toolResult);
        when(comparedAnalysis.getToolMetrics()).thenReturn(List.of(metricContainer));
        when(comparedAnalysis.getAnalysis()).thenReturn(new Analysis(1L));
        comparedAnalysisService.initToolResults();

        for (MetricContainer toolMetric : comparedAnalysis.getToolMetrics()) {
            assertThat(toolMetric.getToolResult()).isNotNull();
        }

    }
}