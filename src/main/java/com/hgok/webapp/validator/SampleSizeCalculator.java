package com.hgok.webapp.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


public class SampleSizeCalculator {

    @Getter
    @Setter
    private int populationSize;
    @Getter
    @Setter
    private double marginOfError;

    private final double zScore;

    @Getter
    @Setter
    private String confidenceLevel;

    @Getter
    @Setter
    private double populationProportion;

    @Getter
    private long sampleSize;

    @Getter
    private int stepSize;

    private static final Map<String, Double> confidenceZscore = new HashMap<>(
            Map.of("80%",1.28,"85%",1.44, "90%", 1.65, "95%", 1.96, "99%", 2.58));

    public SampleSizeCalculator(int populationSize, double marginOfError, String confidenceLevel, double populationProportion) {
        this.populationSize = populationSize;
        this.marginOfError = marginOfError;
        this.confidenceLevel = confidenceLevel;
        this.populationProportion = populationProportion;
        this.zScore = confidenceZscore.get(confidenceLevel);
    }

    public void calculate(){
        double numerator = Math.pow(zScore,2) * populationProportion * (1 - populationProportion);
        double errorSquared = Math.pow(marginOfError, 2);
        sampleSize = Math.round((numerator / errorSquared) / (1 + numerator / (errorSquared * populationSize)));
    }

    public int getPointer(int stepSize) {
        return (int) Math.floor(Math.random() * (stepSize - 1));
    }

    public void setStepSize() {
        stepSize = (int) Math.round((double) getPopulationSize() / getSampleSize());
    }


}
