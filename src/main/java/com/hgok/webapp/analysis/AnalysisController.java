package com.hgok.webapp.analysis;


import com.hgok.webapp.compared.LinkState;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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
    String startAnalysis(@RequestParam(value = "name") String[] names, @RequestParam(value = "file") MultipartFile analysisFile) throws IOException, InterruptedException, ExecutionException {
        List<Tool> filteredTools = analysisService.filterTools(names);
        Analysis analysis = new Analysis(filteredTools, "folyamatban", new Timestamp(System.currentTimeMillis()));
        analysisRepository.save(analysis);

        analysisService.startAnalysis(analysis, IOUtils.toByteArray((analysisFile.getInputStream())), analysisFile.getOriginalFilename());

        return "redirect:/listAnalysis";
    }

    @GetMapping("/listAnalysis")
    String showListAnalysis(Model model){
        model.addAttribute("analysisList", analysisService.getOrderedAnalysises());
        model.addAttribute("accepted", LinkState.ACCEPTED);
        model.addAttribute("denied",LinkState.DENIED);
        model.addAttribute("unchecked",LinkState.UNCHECKED);
        return "analysis-list";
    }

}
