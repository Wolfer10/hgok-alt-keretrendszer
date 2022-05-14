package com.hgok.webapp.validator;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.analysis.AnalysisService;
import com.hgok.webapp.compared.Link;
import com.hgok.webapp.compared.LinkRepository;
import com.hgok.webapp.compared.LinkState;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class LinkValidatorService {


    @Autowired
    AnalysisService analysisService;

    @Autowired
    private LinkRepository linkRepository;

    LinkIterator<Link> linkIterator;
    Analysis analysis;
    Long analysisId;

    public List<String> getFoundBysByLink(Long id){
        return linkRepository.getFoundBysByLink(id);
    }

    public void linkStateChooser(String validation) {
        switch (validation) {
            case "unchecked":
                linkIterator.current().setState(LinkState.UNCHECKED);
                break;
            case "accepted" :
                linkIterator.current().setState(LinkState.ACCEPTED);
                analysis.getComparedAnalysis().addToValidatedLinks(linkIterator.current());
                break;
            case "denied":
                linkIterator.current().setState(LinkState.DENIED);
                analysis.getComparedAnalysis().addToValidatedLinks(linkIterator.current());
                break;
            default:
                throw new IllegalArgumentException("Hibás validiációs értél");
        }
    }

    /**
     * analysis betöltése id alapján
     * iterator inicializálása az analysis linkjeivel
     * a modelnek átadjuk az első linket
     */
    public void init(@PathVariable("id") Long id, Model model) {
        if(analysis == null || !analysisId.equals(id)){
            analysisId = id;
            analysis = init_analysis(id);
            init_iterator();
        }
        model.addAttribute("currentLink", linkIterator.current());
        model.addAttribute("foundBy", getFoundBysByLink(linkIterator.current().getId()));
        init_model(model);
    }

    private void init_model(Model model) {
        model.addAttribute("linkIterator", linkIterator);
        model.addAttribute("analyis", analysis);
    }



    private void init_iterator() {
        List<Link> links = analysis.getComparedAnalysis().getLinks();
        linkIterator = new LinkIterator<>(links);
        setStepSizeAndPointer();

    }

    private void setStepSizeAndPointer() {
        SampleSizeCalculator sizeCalculator = new SampleSizeCalculator(linkIterator.getSize(), 0.05, "80%", 0.5);
        sizeCalculator.calculate();
        sizeCalculator.setStepSize();
        linkIterator.setStepSize(sizeCalculator.getStepSize());
        linkIterator.setCurrentPointer(sizeCalculator.getPointer(linkIterator.getStepSize()));
    }

    private Analysis init_analysis(@PathVariable("id") Long id) {
        Analysis analysis = analysisService.findAnalysisById(id);
        Hibernate.initialize(analysis.getComparedAnalysis().getLinks());
        return analysis;
    }

}
