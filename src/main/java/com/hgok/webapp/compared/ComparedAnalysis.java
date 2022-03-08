package com.hgok.webapp.compared;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hgok.webapp.analysis.Analysis;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.util.JsonUtil;
import com.hgok.webapp.util.NtoMReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ComparedAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Analysis analysis;

    private String fileName;

    private Timestamp validationTime;

    @SerializedName("directed")
    @Expose
    public Boolean directed;
    @SerializedName("multigraph")
    @Expose
    public Boolean multigraph;

    @SerializedName("nodes")
    @Expose
    @OneToMany(fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    public List<Node> nodes = null;

    @SerializedName("links")
    @Expose
    @OneToMany(fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    public List<Link> links = null;

    @OneToMany(fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<MetricContainer> metricContainers = new ArrayList<>();

    public void addMetricContainer(MetricContainer metricContainer) {
        metricContainers.add(metricContainer);
    }

    public void addAllMetricContainer(List<MetricContainer> containers) {
        metricContainers.addAll(containers);
    }

    public void clerMetricContainers() {
        metricContainers.clear();
    }

    public static ComparedAnalysis initComparedAnalysis(Path filePath, Analysis analysis) {
        ComparedAnalysis comparedAnalysis = new ComparedAnalysis();
        try {
            comparedAnalysis = JsonUtil.getComparedToolsFromJson(filePath.toString());
            comparedAnalysis.setValidationTime(new Timestamp(System.currentTimeMillis()));
            comparedAnalysis.setFileName(filePath.getFileName().toString());
            comparedAnalysis.setAnalysis(analysis);
            comparedAnalysis.setLinkSourceAndTarget();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return comparedAnalysis;
    }

    public void setLinkSourceAndTarget() {
        List<Label> labels = new ArrayList<>();
        getLinks().forEach(link -> labels.add(new Label(link.getLabel(), link)));
        labels.forEach(label -> {
            NtoMReader ntoMSourceReader = new NtoMReader(label.getSourceFileName());
            NtoMReader ntoMTargetReader = new NtoMReader(label.getTargetFileName());
            label.getLink().setSourceSnippet(ntoMSourceReader.readFromNToEnd(label.getSourceStartLine()));
            label.getLink().setTargetSnippet(ntoMTargetReader.readFromNToEnd(label.getTargetStartLine()));
        });
    }

    public List<MetricContainer> getToolMetrics(){
        clerMetricContainers();
        for (Tool tool : getAnalysis().getTools()) {
            metricContainers.add(initMetricContainer(tool));
        }
        return metricContainers;
    }

    public MetricContainer initMetricContainer(Tool tool) {
        List<Link> acceptedLinks = getLinks().stream().filter(link -> link.getState() == LinkState.ACCEPTED && new HashSet<>(link.getFoundBy()).contains(tool.getName())).collect(Collectors.toList());
        int all = getLinks().stream().filter(link ->  new HashSet<>(link.getFoundBy()).contains(tool.getName())).toArray().length;
        return new MetricContainer(acceptedLinks.size(), all, tool);
    }

    public void updateMetricContainers(int ourPositive) {
        for (MetricContainer metricContainer : metricContainers) {
             metricContainer.updateContainer(ourPositive);
        }
    }

}