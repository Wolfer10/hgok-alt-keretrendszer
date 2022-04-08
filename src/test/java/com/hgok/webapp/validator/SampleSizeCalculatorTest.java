package com.hgok.webapp.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SampleSizeCalculatorTest {

    SampleSizeCalculator sampleSizeCalculator;

    @BeforeEach
    public void init(){
        sampleSizeCalculator = new SampleSizeCalculator(100000, 0.05, "80%", 0.5);
        sampleSizeCalculator.calculate();

    }

    @Test
    void getSampleSize() {
        assertThat(sampleSizeCalculator.getSampleSize()).isEqualTo(164);
    }

    @Test
    void getStepSize() {
        sampleSizeCalculator.setStepSize();
        assertThat(sampleSizeCalculator.getStepSize()).isEqualTo(610);
    }

    @Test
    void getPointer() {
        for (int i = 0; i < 1000; i++) {
            assertThat(sampleSizeCalculator.getPointer(610)).isBetween(0, 610);
        }

    }


}