package com.hgok.webapp.compared;

import java.io.File;
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
import com.hgok.webapp.util.FileHelper;
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
    @OneToMany( mappedBy = "comparedAnalysis",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    public List<Node> nodes;

    @SerializedName("links")
    @Expose
    @OneToMany(mappedBy = "comparedAnalysis",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    public List<Link> links;

    @OneToMany(mappedBy = "comparedAnalysis",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Link> validatedLinks = new ArrayList<>();

    public void addToValidatedLinks(Link link){
        validatedLinks.add(link);
    }
    public void clearValidatedLinks(){
       validatedLinks.clear();
    }

    @OneToMany(mappedBy = "comparedAnalysis",
            fetch = FetchType.LAZY,
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
        try {
            final ComparedAnalysis comparedAnalysis = JsonUtil.getComparedToolsFromJson(filePath.toString());
            comparedAnalysis.links.forEach(link -> link.setComparedAnalysis(comparedAnalysis));
            comparedAnalysis.nodes.forEach(node -> node.setComparedAnalysis(comparedAnalysis));
            comparedAnalysis.setValidationTime(new Timestamp(System.currentTimeMillis()));
            comparedAnalysis.setFileName(filePath.getFileName().toString());
            comparedAnalysis.setAnalysis(analysis);
            comparedAnalysis.setLinkSourceAndTarget();
            return comparedAnalysis;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ComparedAnalysis();
        }
    }

    public void setLinkSourceAndTarget() {
        List<Label> labels = new ArrayList<>();
        getLinks().forEach(link -> labels.add(new Label(link.getLabel(), link)));
        labels.forEach(label -> {
            NtoMReader ntoMSourceReader = new NtoMReader(label.getSourceFileName());
            NtoMReader ntoMTargetReader = new NtoMReader(label.getTargetFileName());

            if (analysis.getTargetPathName() != null && new File(analysis.getTargetPathName()).isFile()){
                ntoMSourceReader = new NtoMReader(analysis.getTargetPathName());
                ntoMTargetReader = new NtoMReader(analysis.getTargetPathName());
            }

            label.getLink().setSourceSnippet(ntoMSourceReader.readFromNToEnd(label.getSourceStartLine()));
            label.getLink().setTargetSnippet(ntoMTargetReader.readFromNToEnd(label.getTargetStartLine()));
            label.getLink().setSourceRelativeFileName(FileHelper.getRelativeNameFromLink(label.getSourceFileName()));
            label.getLink().setTargetRelativeFileName(FileHelper.getRelativeNameFromLink(label.getTargetFileName()));
            label.getLink().setSourceStartLine(label.getSourceStartLine());
            label.getLink().setTargetStartLine(label.getTargetStartLine());
        });
    }

    public List<MetricContainer> getToolMetrics(){
        clerMetricContainers();
        for (Tool tool : getAnalysis().getTools()) {
            metricContainers.add(initMetricContainer(tool));
        }
        return metricContainers;
    }

    private MetricContainer initMetricContainer(Tool tool) {
        List<Link> acceptedLinks = validatedLinks.stream().filter(link -> link.getState() == LinkState.ACCEPTED && new HashSet<>(link.getFoundBy()).contains(tool.getName())).collect(Collectors.toList());
        int all = validatedLinks.stream().filter(link ->  new HashSet<>(link.getFoundBy()).contains(tool.getName())).toArray().length;
        return new MetricContainer(acceptedLinks.size(), all, tool);
    }

    public void updateMetricContainers(int ourPositive) {
        for (MetricContainer metricContainer : metricContainers) {
             metricContainer.updateContainer(ourPositive);
        }
    }

}