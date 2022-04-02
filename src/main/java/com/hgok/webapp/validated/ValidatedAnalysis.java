package com.hgok.webapp.validated;

import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.compared.Link;
import com.hgok.webapp.compared.MetricContainer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ValidatedAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    Analysis analysis;

    @OneToMany
    public List<Link> validatedLinks;

    @OneToMany
    private List<MetricContainer> metricContainers = new ArrayList<>();


}
