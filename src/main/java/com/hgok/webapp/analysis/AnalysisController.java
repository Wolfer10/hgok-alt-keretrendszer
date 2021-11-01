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
    String startAnalysis(@RequestParam(value = "name") String[] names) throws IOException, InterruptedException {
        List<Tool> filteredTools = new ArrayList<>();
        List<Tool> tools = toolRepository.findAll();
        for (String name : names) {
            filteredTools.addAll(tools.stream().filter(tool -> tool.getName().equals(name)).collect(Collectors.toList()));
        }

        for (Tool filteredTool : filteredTools) {
            analysisService.startProcess(filteredTool.getArguments(), filteredTool.getName());
        }
        return "redirect:/startAnalysis";
    }
}
