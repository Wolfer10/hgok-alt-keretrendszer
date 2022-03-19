package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolResult;
import com.hgok.webapp.tool.ToolResultRepository;
import com.hgok.webapp.tool.ToolResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ComparedAnalysisService {


    ToolResultService toolResultService;

    ComparedAnalysis comparedAnalysis;


    @Autowired
    public ComparedAnalysisService(ToolResultService toolResultService) {
        this.toolResultService = toolResultService;
    }

    public ComparedAnalysisService(ToolResultService toolResultService, ComparedAnalysis comparedAnalysis) {
        this.toolResultService = toolResultService;
        this.comparedAnalysis = comparedAnalysis;
    }

    public void initToolResults(){
        comparedAnalysis.getToolMetrics().forEach(metricContainer -> {
            metricContainer.setToolResult(toolResultService.getToolResultByToolId(metricContainer.getTool().getId(), comparedAnalysis.getAnalysis().getId()));
        });
    }


}
