package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.hcg.ProcessHandler;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolRepository;
import com.hgok.webapp.util.FileHelper;
import com.hgok.webapp.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AnalysisService {

    public static final String WORKINGPATH = "src/main/resources/static/working-dir/";

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private ToolRepository toolRepository;


    public List<Analysis> getOrderedAnalysises(){
        return StreamSupport.stream(analysisRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Analysis::getTimestamp).reversed()).collect(Collectors.toList());
    }

    @Async("asyncExecutor")
    public void startAnalysis(Analysis analysis, byte[] file, String originFileName) throws IOException, InterruptedException {
        System.out.println("Execute method asynchronously. "
                + Thread.currentThread().getName());


        List<Path> filePaths = saveFileIntoSourceFolder(file, originFileName);

        analysis.runEachToolsOnEachFiles(analysis.getTools(), filePaths);
        JsonUtil.dumpToolNamesIntoJson(analysis.getTools(), WORKINGPATH);

        analysis.setFileNames(filePaths.stream().map(filePath -> filePath.getFileName().toString()).collect(Collectors.toList()));

        ProcessHandler processHandler = new ProcessHandler();

        processHandler.startHCGCompare(WORKINGPATH);

        analysis.setComparedAnalysis(
                ComparedAnalysis.initComparedAnalysis(
                        Path.of(FileHelper.COMPARED_FOLDER, analysis.getId() + ".json"),
                        analysis));
        analysis.setStatus("kész");

        analysisRepository.save(analysis);
    }

    private List<Path> saveFileIntoSourceFolder(byte[] file, String fileName) throws IOException {
        FileHelper fileHelper = new FileHelper();
        resetSourceFilesFolder(fileHelper);

        fileHelper.setFilePath(fileHelper.writeBytesIntoNewFile(FileHelper.SOURCE_FOLDER, fileName, file));
        return fileHelper.getFilePaths();
    }

    private void resetSourceFilesFolder(FileHelper fileHelper) throws IOException {
        fileHelper.removeDirByName(FileHelper.SOURCE_FOLDER, "sourceFiles");
        fileHelper.createDirectoryFromName(FileHelper.SOURCE_FOLDER, "sourceFiles");
    }

    public List<Tool> filterTools(String[] toolNames) {
        List<Tool> filteredTools = new ArrayList<>();
        List<Tool> tools = toolRepository.findAll();
        for (String name : toolNames) {
            filteredTools.addAll(tools.stream().filter(tool -> tool.getName().equals(name)).collect(Collectors.toList()));
        }
        return filteredTools;
    }

}
