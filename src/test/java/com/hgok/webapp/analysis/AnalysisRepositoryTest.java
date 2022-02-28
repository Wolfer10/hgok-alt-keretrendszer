package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.compared.ComparedAnalysisRepository;
import com.hgok.webapp.compared.ComparedAnalysisService;
import com.hgok.webapp.compared.MetricContainer;
import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnalysisRepositoryTest {

    @Autowired
    AnalysisRepository analysisRepository;

    @Autowired
    ComparedAnalysisRepository comparedAnalysisRepository;

    private Analysis analysis;


    @BeforeEach
    public void init() {
       analysis = analysisRepository.findById(1L).orElseThrow(
               () -> new IllegalArgumentException("Invalid user Id"));

    }
    @Test
    public void testFindById(){
        assertThat(analysis.getComparedAnalysises()).hasSize(1);
        assertThat(analysis.getComparedAnalysises().get(0).getFileName()).isEqualTo("TestFile.json");
        assertThat(analysis.getComparedAnalysises().get(0).getToolMetrics()).hasSize(1);
    }

    @Test
    public void testFind(){
        assertThat(comparedAnalysisRepository.findAllByAnalysis_Id(1L)).hasSize(1);
    }



    @Test
    public void testUpdateMetricContainers(){
        assertThat(analysis.getComparedAnalysises()).isNotEmpty();
        for (ComparedAnalysis comparedAnalysise : analysis.getComparedAnalysises()) {
            comparedAnalysise.updateMetricContainers(10);
        }
        for (ComparedAnalysis comparedAnalysise : analysis.getComparedAnalysises()) {
            for (MetricContainer metricContainer : comparedAnalysise.getMetricContainers()) {
                assertThat(metricContainer).isNotNull();
                assertThat(metricContainer.getOurTruePositive()).isEqualTo(10);
            }
        }

    }

}