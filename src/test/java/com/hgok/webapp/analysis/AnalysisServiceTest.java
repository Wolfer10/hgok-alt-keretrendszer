package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class AnalysisServiceTest {

    @Test
    void testUpdateAnalysis() {
        Analysis analysis = new Analysis(54L);
        analysis.setFileNames(new ArrayList<>(List.of("Testfile3.js")));
        analysis.setComparedAnalysis(new ComparedAnalysis());
        Analysis expectedAnalysis = analysis.updateAnalysis();

        ComparedAnalysis expectedComparedAnalysis = expectedAnalysis.getComparedAnalysis();

        assertThat(expectedComparedAnalysis).isNotNull();
        assertThat(expectedComparedAnalysis.directed).isTrue();
        assertThat(expectedComparedAnalysis.getAnalysis()).isEqualTo(analysis);
        assertThat(expectedComparedAnalysis.getLinks()).hasSize(5);

        Assertions.assertEquals(expectedAnalysis.getStatus(), "kész");
    }
}