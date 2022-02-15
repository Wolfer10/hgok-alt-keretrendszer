package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.Link;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisServiceTest {


    void testInitLinks() {
        AnalysisService analysisService = new AnalysisService();
        Analysis analysis = new Analysis(186L);
        //analysis.getComparedAnalysis().setLinks(new ArrayList<>());
        //analysisService.setLinkSourceAndTarget();
    }
}