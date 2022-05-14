package com.hgok.webapp.compared;

import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class MetricContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity=ComparedAnalysis.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "compared_analysis_id")
    private ComparedAnalysis comparedAnalysis;

    @ManyToOne(targetEntity=Tool.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @OneToOne(fetch = FetchType.LAZY)
    private ToolResult toolResult;

    private int truePositive;
    private int all;
    private int ourTruePositive;
    private double precision;
    private double recall;
    private double fMeasure;
    private Time runningTime;


    public MetricContainer(int truePositive, int all, Tool tool) {
        this.truePositive = truePositive;
        this.all = all;
        this.precision = calculatePrecision(truePositive, all);
        this.tool = tool;
    }

    public double calculatePrecision(int truePositive, int all){
        return (double) truePositive / (double) all;
    }

    public void setRecall(int truePositive, int ourTruePositive){
        if (ourTruePositive == 0){
            recall = 0.0;
            return;
        }
        recall = (double) truePositive / (double) ourTruePositive;
    }

    public void setFMeasure(double precision, double recall){
        fMeasure = 2 * (  precision * recall /  precision + recall);
    }

    public void updateContainer(int ourPositive) {
        setOurTruePositive(ourPositive);
        setRecall(getTruePositive(), ourPositive);
        setFMeasure(getPrecision(), getRecall());
    }
}
