package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.analysis.AnalysisRepository;
import com.hgok.webapp.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ComparedAnalysisService {

    public List<MetricContainer> getToolMetrics(Analysis analysis){
        List<MetricContainer> metricContainers = new ArrayList<>();
        for (Tool tool : analysis.getTools()) {
            metricContainers.add(initMetricContainer(analysis.getComparedAnalysis(), tool.getName()));
        }
        return metricContainers;
    }

    private MetricContainer initMetricContainer(ComparedAnalysis comparedAnalysis, String toolName) {
        int truePositive = comparedAnalysis.getLinks().stream().filter(link -> link.getState() == LinkState.ACCEPTED ).toArray().length;
        int all = comparedAnalysis.getLinks().size();
        return new MetricContainer(truePositive, all, toolName);
    }

}
