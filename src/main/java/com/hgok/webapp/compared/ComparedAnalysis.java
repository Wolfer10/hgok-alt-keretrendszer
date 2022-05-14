package com.hgok.webapp.compared;

import java.io.*;
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
import com.hgok.webapp.util.*;
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

    public ComparedAnalysis(Analysis analysis, String fileName) {
        this.analysis = analysis;
        this.fileName = fileName;
    }

    @SerializedName("directed")
    @Expose
    public Boolean directed;
    @SerializedName("multigraph")
    @Expose
    public Boolean multigraph;

    @SerializedName("nodes")
    @Expose
    @OneToMany(mappedBy = "comparedAnalysis",
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

    public void addToValidatedLinks(Link link) {
        validatedLinks.add(link);
    }

    public void clearValidatedLinks() {
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

            if (analysis.getTargetPathName() != null && new File(analysis.getTargetPathName()).isFile()) {
                ntoMSourceReader = new NtoMReader(analysis.getTargetPathName());
                ntoMTargetReader = new NtoMReader(analysis.getTargetPathName());
            }
            Link link = label.getLink();
            link.setSourceSnippet(ntoMSourceReader.readFromNtoEnd(label.getSourceStartLine()));
            link.setTargetSnippet(ntoMTargetReader.readFromNtoEnd(label.getTargetStartLine()));

            FunctionExtractor extractor = new JsFunctionExtractor();
            FunctionNameExtractor nameExtractor = new SimpleFunctionNameExtractor();
            String relativeNameFromLinkSource = getRelativeNameFromLink(label.getSourceFileName());
            String relativeNameFromLinkTarget = getRelativeNameFromLink(label.getTargetFileName());


            if (label.getSourceStartLine() == 0 && label.getTargetStartLine() == 0) {
                link.setSourceFunction(ntoMSourceReader.readFromNtoEnd(label.getSourceStartLine()));
                link.setTargetFunction(ntoMTargetReader.readFromNtoEnd(label.getTargetStartLine()));
                label.getLink().setSourceFunctionName(getNpmName(relativeNameFromLinkSource));
                label.getLink().setTargetFunctionName(getNpmName(relativeNameFromLinkTarget));

            } else {
                setSourceFunctionBody(label, ntoMSourceReader, extractor);
                setTargetFunctionBody(label, ntoMTargetReader, extractor);
                link.setSourceFunctionName(nameExtractor
                        .extractFunctionName(ntoMSourceReader.getStringNthLine(link.getSourceSnippet(), 1)));
                link.setTargetFunctionName(nameExtractor
                        .extractFunctionName(ntoMTargetReader.getStringNthLine(link.getTargetSnippet(), 1)));
            }

            link.setMatched(nameExtractor
                    .extractFunctionNamesFromBody(link.getSourceFunction())
                    .contains(link.getTargetFunctionName()));
            link.setSourceRelativeFileName(relativeNameFromLinkSource);
            link.setTargetRelativeFileName(relativeNameFromLinkTarget);
            link.setSourceStartLine(label.getSourceStartLine());
            link.setTargetStartLine(label.getTargetStartLine());
        });
    }
    //[TestFile3]main:0
    public static String getNpmName(String headerName) {
        return headerName.substring(headerName.indexOf(']') + 1, headerName.indexOf(':'));
    }

    private String getRelativeNameFromLink(String label) {
        return FileHelper.getRelativeNameFromLink(label);
    }

    private void setSourceFunctionBody(Label label, NtoMReader ntoMReader, FunctionExtractor extractor) {
        try (BufferedReader br = new BufferedReader(new StringReader(label.getLink().getSourceSnippet()))) {
            label.getLink().setSourceFunction(ntoMReader.readFromNLineToMCharacter(br, 0,
                    extractor.extractFunctionEnd(label.getLink().getSourceSnippet(), 0)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTargetFunctionBody(Label label, NtoMReader ntoMReader, FunctionExtractor extractor) {
        try (BufferedReader br = new BufferedReader(new StringReader(label.getLink().getTargetSnippet()))) {
            label.getLink().setTargetFunction(ntoMReader.readFromNLineToMCharacter(br,
                    0, extractor.extractFunctionEnd(label.getLink().getTargetSnippet(), 0)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<MetricContainer> getToolMetrics() {
        clerMetricContainers();
        for (Tool tool : getAnalysis().getTools()) {
            metricContainers.add(initMetricContainer(tool));
        }
        return metricContainers;
    }

    private MetricContainer initMetricContainer(Tool tool) {
        List<Link> acceptedLinks = validatedLinks.stream().filter(link -> link.getState() == LinkState.ACCEPTED && new HashSet<>(link.getFoundBy()).contains(tool.getName())).collect(Collectors.toList());
        int all = validatedLinks.stream().filter(link -> new HashSet<>(link.getFoundBy()).contains(tool.getName())).toArray().length;
        return new MetricContainer(acceptedLinks.size(), all, tool);
    }

    public void updateMetricContainers(int ourPositive) {
        for (MetricContainer metricContainer : metricContainers) {
            metricContainer.updateContainer(ourPositive);
        }
    }

}