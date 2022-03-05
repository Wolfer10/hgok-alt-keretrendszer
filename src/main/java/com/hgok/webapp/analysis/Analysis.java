package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolResult;
import com.hgok.webapp.util.FileHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

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

    @OneToOne(
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

    public void runEachToolsOnEachFiles(List<Tool> filteredTools, List<Path> filePaths) throws IOException, InterruptedException {
        FileHelper fileHelper = new FileHelper();
        for(Tool filteredTool : filteredTools) {
            new FileHelper().removeDirByName(WORKINGPATH, filteredTool.getName());
            Path pathOfResult = fileHelper.createDirAndInsertFile(Path.of(WORKINGPATH), filteredTool.getName(), String.valueOf(id));
            ToolResult toolResult = new ToolResult(filteredTool, filePaths);
            fileHelper.appendToFile(pathOfResult, toolResult.getResult());
            ProcessHandler processHandler = new ProcessHandler();
            processHandler.startHCGConvert(pathOfResult.getParent());

        }
    }

}
