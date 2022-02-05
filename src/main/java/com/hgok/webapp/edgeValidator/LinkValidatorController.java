package com.hgok.webapp.edgeValidator;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.analysis.AnalysisRepository;
import com.hgok.webapp.compared.Link;
import com.hgok.webapp.tool.Tool;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LinkValidatorController {

    @Autowired
    private AnalysisRepository analysisRepository;

    LinkIterator<Link> linkIterator;
    Analysis analysis;
    Long analysisId;

    @GetMapping("/validate/{analysisId}/currlink/{id}")
    public String showCodeSnippetsToValidate(@PathVariable("analysisId") Long id, Model model) {
        init(id, model);
        return "validate-analysis";
    }

    @GetMapping("/validate/{analysisId}/nextlink/{id}")
    public String stepForward() {
        return String.format("redirect:/validate/%s/currlink/%s", analysis.getId(), linkIterator.next().getId());
    }

    @GetMapping("/validate/{analysisId}/prevlink/{id}")
    public String stepBackwards() {
        return String.format("redirect:/validate/%s/currlink/%s", analysis.getId(), linkIterator.previous().getId());
    }

    @PostMapping("/validate/end")
    public String end() {
        analysis.setLinks(linkIterator.getList());
        analysis.setStatus("validated");
        analysisRepository.save(analysis);
        return "redirect:/listAnalysis";
    }

    @PostMapping("/validateLink")
    @ResponseBody
    public void validateLink(@RequestParam(name = "valid_link") String validation){
        linkIterator.current().setAccepted(validation);
    }


    /**
     * analysis betöltése id alapján
     * iterator inicializálása az analysis linkjeivel
     * a modelnek átadjuk az első linket
     */
    private void init(@PathVariable("id") Long id, Model model) {
        if(analysis == null || !analysisId.equals(id)){
            analysisId = id;
            analysis = init_analysis(id);
            init_iterator();
        }
        model.addAttribute("currentLink", linkIterator.current());
        init_model(model);
    }

    private void init_model(Model model) {
        model.addAttribute("linkIterator", linkIterator);
        model.addAttribute("analyis", analysis);
    }

    private void init_iterator() {
        //System.out.println(analysis.getLinks());
        linkIterator = new LinkIterator<>(analysis.getLinks());
    }

    private Analysis init_analysis(@PathVariable("id") Long id) {
        return analysisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid analysis Id:" + id));
    }
}
