package com.hgok.webapp.validator;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.analysis.AnalysisService;
import com.hgok.webapp.compared.Link;
import com.hgok.webapp.compared.LinkRepository;
import com.hgok.webapp.compared.LinkState;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LinkValidatorController {


    @Autowired
    private LinkValidatorService linkValidatorService;

    @GetMapping("/validate/{analysisId}/currlink/{id}")
    public String showCodeSnippetsToValidate(@PathVariable("analysisId") Long id, Model model) {
        linkValidatorService.init(id, model);
        model.addAttribute("accepted", LinkState.ACCEPTED);
        model.addAttribute("denied",LinkState.DENIED);
        model.addAttribute("unchecked",LinkState.UNCHECKED);
        return "validate-analysis";
    }

    @GetMapping("/validate/{analysisId}/nextlink/{id}")
    public String stepForward(Model model) {
        Link next = linkValidatorService.linkIterator.next();
        model.addAttribute("foundBy", linkValidatorService.getFoundBysByLink(next.getId()));
        return String.format("redirect:/validate/%s/currlink/%s", linkValidatorService.analysis.getId(), next.getId());
    }

    @GetMapping("/validate/{analysisId}/prevlink/{id}")
    public String stepBackwards(Model model) {
        Link previous = linkValidatorService.linkIterator.previous();
        model.addAttribute("foundBy", linkValidatorService.getFoundBysByLink(previous.getId()));
        return String.format("redirect:/validate/%s/currlink/%s", linkValidatorService.analysis.getId(), previous.getId());
    }

    @PostMapping("/validate/end")
    public String end() {
        linkValidatorService.analysis.getComparedAnalysis().setLinks(linkValidatorService.linkIterator.getList());
        linkValidatorService.analysis.setStatus("validated");
        linkValidatorService.analysisService.saveAnalysis(linkValidatorService.analysis);
        return "redirect:/listAnalysis";
    }

    @PostMapping("/validateLink")
    @ResponseBody
    public void validateLink(@RequestParam(name = "valid_link") String validation){
        linkValidatorService.linkStateChooser(validation);
    }

}
