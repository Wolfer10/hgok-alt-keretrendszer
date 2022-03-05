package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.hcg.ProcessHandler;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolRepository;
import com.hgok.webapp.util.FileHelper;
import com.hgok.webapp.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AnalysisService {


    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    public static final String WORKINGPATH = "src/main/resources/static/working-dir/";

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private ToolRepository toolRepository;


    public List<Analysis> getOrderedAnalysises(){
        return StreamSupport.stream(analysisRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Analysis::getTimestamp).reversed()).collect(Collectors.toList());
    }


    public void startAnalysis(String[] toolNames, MultipartFile analysisFile) throws IOException, ExecutionException, InterruptedException {

        List<Tool> filteredTools = filterTools(toolNames);

        Analysis analysis = new Analysis(filteredTools, "folyamatban", new Timestamp(System.currentTimeMillis()));

        analysisRepository.save(analysis);

        FileHelper fileHelper = new FileHelper();

        fileHelper.removeDirByName(FileHelper.SOURCE_FOLDER, "sourceFiles");
        fileHelper.createDirectoryFromName(FileHelper.SOURCE_FOLDER, "sourceFiles");
        fileHelper.saveMultipartFile(FileHelper.SOURCE_FOLDER, analysisFile);

        List<Path> filePaths = fileHelper.unzipAndGetFiles();

        runEachToolsOnEachFiles(filteredTools, filePaths, analysis.getId());

        JsonUtil.dumpToolNamesIntoJson(filteredTools, WORKINGPATH);


        analysis.setFileNames(filePaths.stream().map(filePath -> filePath.getFileName().toString()).collect(Collectors.toList()));

        executeComparison()
                .thenRun(() -> {
                    analysis.setComparedAnalysis(ComparedAnalysis.initComparedAnalysis(
                            Path.of(FileHelper.COMPARED_FOLDER,
                                    analysis.getId() + ".json"), analysis));
                    analysis.setStatus("kész");
                    analysisRepository.save(analysis);
                })
                .exceptionally((ex) -> {
                    ex.printStackTrace(System.err);
                    return null;
                }).get();
    }

    public void runEachToolsOnEachFiles(List<Tool> filteredTools, List<Path> filePaths, long analId) throws IOException {
        FileHelper fileHelper = new FileHelper();
        for(Tool filteredTool : filteredTools) {
            new FileHelper().removeDirByName(WORKINGPATH, filteredTool.getName());
            Path pathOfResult = fileHelper.createDirAndInsertFile(Path.of(WORKINGPATH), filteredTool.getName(), String.valueOf(analId));
            fileHelper.appendToFile(pathOfResult, filteredTool.getToolResult(filePaths).getResult());
            executeConversion(filteredTool.getName(), pathOfResult.getParent());
        }
    }



    private List<Tool> filterTools(String[] toolNames) {
        List<Tool> filteredTools = new ArrayList<>();
        List<Tool> tools = toolRepository.findAll();
        for (String name : toolNames) {
            filteredTools.addAll(tools.stream().filter(tool -> tool.getName().equals(name)).collect(Collectors.toList()));
        }
        return filteredTools;
    }

    private CompletableFuture<Void> executeComparison() {
        return CompletableFuture.runAsync(() -> {
            try {
                new ProcessHandler().startHCGCompare(WORKINGPATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, Exocutor.executor );
    }

    private CompletableFuture<?> executeConversion(String toolName, Path dir) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.error(toolName + " kezdete");
                new ProcessHandler().startHCGConvert(dir);
                log.error(toolName + " vége");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, Exocutor.executor );
    }
}
