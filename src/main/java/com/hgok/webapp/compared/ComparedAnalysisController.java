package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.analysis.AnalysisRepository;
import com.hgok.webapp.analysis.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ComparedAnalysisController {
    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private ComparedAnalysisService comparedAnalysisService;

    Analysis actualAnalysis;

    @GetMapping("/statistics/{analysisId}")
    public String showStats(@PathVariable("analysisId") Long id, Model model) {
        if(actualAnalysis == null){
            actualAnalysis = analysisRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid analysis Id:" + id));
            actualAnalysis.getComparedAnalysis().setMetricContainers(comparedAnalysisService.getToolMetrics(actualAnalysis));
        }
        model.addAttribute("analysis", actualAnalysis );
        model.addAttribute("metrics", actualAnalysis.getComparedAnalysis().getMetricContainers());

        return "validated-analysis";
    }

    @PostMapping("/calculateStats")
    public String calculateStats(@RequestParam(value = "ourPositive") int ourPositive){
        List<MetricContainer> metricContainers = comparedAnalysisService.getToolMetrics(actualAnalysis);
        for (MetricContainer metricContainer : metricContainers) {
            metricContainer.setOurTruePositive(ourPositive);
            metricContainer.setRecall(metricContainer.getTruePositive(), ourPositive);
            metricContainer.setFMeasure(metricContainer.getPrecision(), metricContainer.getRecall());
        }
        actualAnalysis.getComparedAnalysis().setMetricContainers(metricContainers);
        return "redirect:/statistics/" + actualAnalysis.getId();
    }

}
