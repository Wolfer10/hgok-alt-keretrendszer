package com.hgok.webapp.compared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class MetricContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String toolName;
    private int truePositive;
    private int all;
    private int ourTruePositive;
    private double precision;
    private double recall;
    private double fMeasure;

    public MetricContainer(int truePositive, int all, String toolName) {
        this.truePositive = truePositive;
        this.all = all;
        this.precision = calculatePrecision(truePositive, all);
        this.toolName = toolName;
    }

    public double calculatePrecision(int truePositive, int all){
        return (double) truePositive / (double) all;
    }
    public void setRecall(int truePositive, int ourTruePositive){
        if (ourTruePositive == 0) recall = 0.0;
        recall = (double) truePositive / (double) ourTruePositive;
    }

    public void setFMeasure(double precision, double recall){
        fMeasure = 2 * (  precision * recall /  precision + recall);
    }
}
