package com.hgok.webapp.analysis;


import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AnalysisController {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private AnalysisService analysisService;
    @Autowired
    private AnalysisRepository analysisRepository;

    @GetMapping("/startAnalysis")
    String showStartAnalysis(Model model){
        model.addAttribute("languages", toolRepository.GroupToolLanguages());
        return "start-analysis";
    }

    @GetMapping("/getNames")
    @ResponseBody
    List<String> getNames(@RequestParam(value = "language") String language){
        return toolRepository.getToolsFromLanguage(language).stream()
                .map(Tool::getName)
                .collect(Collectors.toList());

    }
    @PostMapping("/startAnalysis")
    String startAnalysis(@RequestParam(value = "name") String[] names) throws IOException {
        analysisService.startAnalysis(names);

        return "redirect:/listAnalysis";
    }

    @GetMapping("/listAnalysis")
    String showListAnalysis(Model model){
        model.addAttribute("analysisList", analysisRepository.findAll());
        return "analysis-list";
    }

}
