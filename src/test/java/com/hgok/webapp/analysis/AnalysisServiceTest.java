package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.compared.Link;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class AnalysisServiceTest {

    @Test
    void testUpdateAnalysis() {
        Analysis analysis = new Analysis(54L);
        analysis.setFileNames(new ArrayList<>(List.of("Testfile.js")));
        Analysis expectedAnalysis = analysis.updateAnalysis();

        List<ComparedAnalysis> expectedComparedAnalysises = expectedAnalysis.getComparedAnalysises();

        assertThat(expectedComparedAnalysises).isNotEmpty().hasSize(1);
        assertThat(expectedComparedAnalysises.get(0)).isNotNull();
        assertThat(expectedComparedAnalysises.get(0).directed).isTrue();
        assertThat(expectedComparedAnalysises.get(0).getAnalysis()).isEqualTo(analysis);

        Assertions.assertEquals(expectedAnalysis.getStatus(), "kész");
    }
}