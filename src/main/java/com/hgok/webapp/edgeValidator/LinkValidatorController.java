package com.hgok.webapp.edgeValidator;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.analysis.AnalysisRepository;
import com.hgok.webapp.compared.Link;
import com.hgok.webapp.compared.LinkRepository;
import com.hgok.webapp.compared.LinkState;
import com.hgok.webapp.tool.ToolRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LinkValidatorController {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private LinkRepository linkRepository;

    LinkIterator<Link> linkIterator;
    Analysis analysis;
    Long analysisId;

    @GetMapping("/validate/{analysisId}/currlink/{id}")
    public String showCodeSnippetsToValidate(@PathVariable("analysisId") Long id, Model model) {
        init(id, model);
        model.addAttribute("accepted", LinkState.ACCEPTED);
        model.addAttribute("denied",LinkState.DENIED);
        model.addAttribute("unchecked",LinkState.UNCHECKED);
        return "validate-analysis";
    }

    @GetMapping("/validate/{analysisId}/nextlink/{id}")
    public String stepForward(Model model) {
        Link next = linkIterator.next();
        model.addAttribute("foundBy", linkRepository.getFoundBysByLink(next.getId()));
        return String.format("redirect:/validate/%s/currlink/%s", analysis.getId(), next.getId());
    }

    @GetMapping("/validate/{analysisId}/prevlink/{id}")
    public String stepBackwards(Model model) {
        Link previous = linkIterator.previous();
        model.addAttribute("foundBy", linkRepository.getFoundBysByLink(previous.getId()));
        return String.format("redirect:/validate/%s/currlink/%s", analysis.getId(), previous.getId());
    }

    @PostMapping("/validate/end")
    public String end() {
        analysis.getComparedAnalysis().setLinks(linkIterator.getList());
        analysis.setStatus("validated");
        analysisRepository.save(analysis);
        return "redirect:/listAnalysis";
    }

    @PostMapping("/validateLink")
    @ResponseBody
    public void validateLink(@RequestParam(name = "valid_link") String validation){
        linkStateChooser(validation);
    }

    private void linkStateChooser(String validation) {
        switch (validation) {
            case "unchecked":
                linkIterator.current().setState(LinkState.UNCHECKED);
                break;
            case "accepted" :
                linkIterator.current().setState(LinkState.ACCEPTED);
                break;
            case "denied":
                linkIterator.current().setState(LinkState.DENIED);
                break;
        }
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
        model.addAttribute("foundBy", linkRepository.getFoundBysByLink(linkIterator.current().getId()));
        init_model(model);
    }

    private void init_model(Model model) {
        model.addAttribute("linkIterator", linkIterator);
        model.addAttribute("analyis", analysis);
    }



    private void init_iterator() {
        linkIterator = new LinkIterator<>(analysis.getComparedAnalysis().getLinks());

    }

    private Analysis init_analysis(@PathVariable("id") Long id) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid analysis Id:" + id));
        Hibernate.initialize(analysis.getComparedAnalysis().getLinks());
        return analysis;
    }
}
