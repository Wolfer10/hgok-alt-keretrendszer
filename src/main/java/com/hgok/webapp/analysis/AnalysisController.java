package com.hgok.webapp.analysis;


import com.hgok.webapp.compared.LinkState;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class AnalysisController {

    @Autowired
    private ToolService toolService;

    @Autowired
    private AnalysisService analysisService;


    @GetMapping("/startAnalysis")
    String showStartAnalysis(Model model){
        model.addAttribute("languages", toolService.groupToolLanguages());
        return "start-analysis";
    }

    @GetMapping("/getNames")
    @ResponseBody
    List<String> getNames(@RequestParam(value = "language") String language){
        return toolService.getToolNamesFromLanguage(language);

    }
    @PostMapping("/startAnalysis")
    String startAnalysis(@RequestParam(value = "name") String[] names, @RequestParam(value = "file") MultipartFile analysisFile) throws IOException, InterruptedException, ExecutionException {
        List<Tool> filteredTools = analysisService.filterTools(names);
        String originalFilename = analysisFile.getOriginalFilename();
        analysisService.setAnalysis(filteredTools, originalFilename);
        analysisService.saveAnalysis();
        analysisService.startAnalysis(IOUtils.toByteArray((analysisFile.getInputStream())), originalFilename);

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
