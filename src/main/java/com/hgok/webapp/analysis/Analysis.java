package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.compared.Link;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.util.FileHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(
            cascade = CascadeType.ALL)
    private List<Tool> tools;

    @ElementCollection
    private List<String> fileNames;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<ComparedAnalysis> comparedAnalysises = new ArrayList<>();

    public void addAllComparedAnalysises(List<ComparedAnalysis> comparedAnalysis){
        comparedAnalysises.addAll(comparedAnalysis);
    }

    private String status;

    private Timestamp timestamp;


    public Analysis(List<Tool> tools, String status, Timestamp timestamp) {
        this.tools = tools;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Analysis(Long id) {
       this.id = id;
    }

    public List<Link> getAllLinksFromCompareds() {
        return comparedAnalysises.stream()
                .map(ComparedAnalysis::getLinks)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Analysis updateAnalysis()  {
        ComparedAnalysis comparedAnalysis = new ComparedAnalysis();
        FileHelper fileHelper = new FileHelper();
        List<ComparedAnalysis> comparedAnalyses = getFileNames().stream()
                .map(fileName -> comparedAnalysis.initComparedAnalysis(
                        Path.of(FileHelper.COMPARED_FOLDER,
                                fileHelper.replaceFormat(fileName, ".json")), this))
                .collect(Collectors.toList());
        setComparedAnalysises(comparedAnalyses);
        setStatus("kész");
        return this;
    }

}
