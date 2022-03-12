package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.analysis.AnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComparedAnalysisController {
    @Autowired
    private AnalysisRepository analysisRepository;

    private ComparedAnalysisService comparedAnalysisService;

    @Autowired
    public ComparedAnalysisController(ComparedAnalysisService comparedAnalysisService) {
        this.comparedAnalysisService = comparedAnalysisService;
    }

    Analysis actualAnalysis;
    Long analysisId;

    @GetMapping("/statistics/{analysisId}")
    public String showStats(@PathVariable("analysisId") Long id, Model model) {
        initAnalysis(id);
        model.addAttribute("compared", actualAnalysis.getComparedAnalysis());
        analysisRepository.save(actualAnalysis);
        return "validated-analysis";
    }

    @PostMapping("/calculateStats")
    public String calculateStats(@RequestParam(value = "ourPositive") int ourPositive){
        actualAnalysis.getComparedAnalysis().updateMetricContainers(ourPositive);
        return "redirect:/statistics/" + actualAnalysis.getId();
    }

    private void initAnalysis(Long id) {
        if(actualAnalysis == null || !analysisId.equals(id)){
            analysisId = id;
            actualAnalysis = analysisRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid analysis Id:" + id));
            actualAnalysis.getComparedAnalysis().getToolMetrics();
            comparedAnalysisService.comparedAnalysis = actualAnalysis.getComparedAnalysis();
            comparedAnalysisService.initToolResults();
        }
    }


}
