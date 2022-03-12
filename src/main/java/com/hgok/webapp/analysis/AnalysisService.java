package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.hcg.ProcessHandler;
import com.hgok.webapp.tool.*;
import com.hgok.webapp.util.FileHelper;
import com.hgok.webapp.util.JsonUtil;
import com.hgok.webapp.util.ZipReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AnalysisService {

    public static final String WORKINGPATH = "src/main/resources/static/working-dir/";

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolResultRepository toolResultRepository;


    @Autowired
    private MemoryDataRepository memoryDataRepository;


    public List<Analysis> getOrderedAnalysises(){
        return StreamSupport.stream(analysisRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Analysis::getTimestamp).reversed()).collect(Collectors.toList());
    }

    @Async("single-thread")
    public void startAnalysis(Analysis analysis, byte[] file, String originFileName) throws IOException, InterruptedException, ExecutionException {
        System.out.println("Execute method asynchronously. "
                + Thread.currentThread().getName());


        Path insertedFilePath = initFileInNewSourceFolder(originFileName);
        Files.write(insertedFilePath, file);


        FileHelper fileHelper = new FileHelper();
        Path targetPath = fileHelper.createDirectoryFromName(WORKINGPATH, originFileName.split("\\.")[0] + analysis.getId());
        if (originFileName.endsWith(".zip")){
            ZipReader.unzip(insertedFilePath, targetPath);
        } else {
            Path tempPath = Files.createFile(Path.of(String.valueOf(targetPath), originFileName));
            Files.copy(insertedFilePath, tempPath, StandardCopyOption.REPLACE_EXISTING);
            targetPath = tempPath;
        }

        runEachToolsOnTarget(analysis.getTools(), analysis, targetPath);

        JsonUtil.dumpToolNamesIntoJson(analysis.getTools(), WORKINGPATH);

        ProcessHandler processHandler = new ProcessHandler();

        processHandler.startHCGCompare(WORKINGPATH);

        analysis.setComparedAnalysis(
                ComparedAnalysis.initComparedAnalysis(
                        Path.of(FileHelper.COMPARED_FOLDER, analysis.getId() + ".json"),
                        analysis));
        analysis.setStatus("kész");

        analysisRepository.save(analysis);
    }

    private Path initFileInNewSourceFolder(String originFileName) throws IOException {
        FileHelper fileHelper = new FileHelper();
        File file1 = new File(FileHelper.SOURCE_FOLDER);
        fileHelper.deleteFileIfExits(file1.toPath());
        return fileHelper.createDirAndInsertFile(Path.of(WORKINGPATH), Path.of(FileHelper.SOURCE_FOLDER).getFileName().toString(), originFileName, "");
    }

    private void runEachToolsOnTarget(List<Tool> tools, Analysis analysis, Path path) throws IOException, ExecutionException, InterruptedException {
        FileHelper fileHelper = new FileHelper();
        for(Tool filteredTool : tools) {
            fileHelper.removeDirByName(WORKINGPATH, filteredTool.getName());
            Path pathOfResult = fileHelper.createDirAndInsertFile(Path.of(WORKINGPATH), filteredTool.getName(), String.valueOf(analysis.getId()), ".cgtxt");
            ToolResult toolResult = new ToolResult(filteredTool, path, analysis);
            toolResult.setMemoryData(toolResult.getMemoryData() != null ? toolResult.getMemoryData() : new MemoryData());
            memoryDataRepository.save(toolResult.getMemoryData());
            toolResultRepository.save(toolResult);
            Files.write(pathOfResult, toolResult.getResult());
            ProcessHandler processHandler = new ProcessHandler();
            processHandler.startHCGConvert(pathOfResult.getParent());
        }
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
