package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.hcg.ProcessHandler;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolResult;
import com.hgok.webapp.util.FileHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import static com.hgok.webapp.analysis.AnalysisService.WORKINGPATH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany()
    private List<Tool> tools;

    @ElementCollection
    private List<String> fileNames;

    @OneToOne(fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private ComparedAnalysis comparedAnalysis;


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


}
