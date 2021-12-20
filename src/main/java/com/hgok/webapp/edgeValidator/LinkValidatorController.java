package com.hgok.webapp.edgeValidator;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.analysis.AnalysisRepository;
import com.hgok.webapp.compared.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LinkValidatorController {

    @Autowired
    private AnalysisRepository analysisRepository;

    LinkIterator<Link> linkIterator;
    Analysis analysis;

    @GetMapping("/validate/{valid}/currLink/{id}")
    public String showCodeSnippetsToValidate(@PathVariable("valid") Long id, Model model) {
        init(id, model);
        return "validate-analysis";
    }

    @GetMapping("/validate/{valid}/nextlink/{id}")
    public String stepForward() {
        return String.format("redirect:/validate/%s/%s", analysis.getLinks(), linkIterator.next().getId());
    }

    @GetMapping("/validate/{valid}/prevlink/{id}")
    public String stepBackwards() {
        return String.format("redirect:/validate/%s/%s", analysis.getLinks(), linkIterator.previous().getId());
    }

    @PostMapping("/validate/link/{id}")
    public String stepForward(@PathVariable("id") Long id, Model model) {

        return "validate-analysis";
    }


    /**
     * analysis betöltése id alapján
     * iterator inicializálása az analysis linkjeivel
     * a modelnek átadjuk az első linket
     */
    private void init(@PathVariable("id") Long id, Model model) {
        analysis = init_analysis(id);
        init_iterator();
        model.addAttribute("currentLink", linkIterator.current());
        init_model(model);
    }
    private void init_model(Model model) {
        model.addAttribute("linkIterator", linkIterator);
        model.addAttribute("analyis", analysis);
    }
    private void init_iterator() {
        System.out.println(analysis.getLinks());
        linkIterator = new LinkIterator<>(analysis.getLinks());
    }
    private Analysis init_analysis(@PathVariable("id") Long id) {
        return analysisRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid analysis Id:" + id));
    }
}
