package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.*;
import com.hgok.webapp.hcg.ProcessHandler;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolRepository;
import com.hgok.webapp.util.FileHelper;
import com.hgok.webapp.util.JsonUtil;
import com.hgok.webapp.util.ZipReader;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.hgok.webapp.util.FileHelper.removeDirByNames;


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
        fileHelper.saveFile(FileHelper.SOURCE_FOLDER, analysisFile);

        List<Path> filePaths = fileHelper.getPaths();

        runEachToolsOnEachFiles(filteredTools, filePaths);

        JsonUtil.dumpToolNamesIntoJson(filteredTools, WORKINGPATH);


        analysis.setFileNames(filePaths.stream().map(filePath -> filePath.getFileName().toString()).collect(Collectors.toList()));

        executeComparison()
                .thenRun(() -> analysisRepository.save(analysis.updateAnalysis()))
                .exceptionally((ex) -> {ex.printStackTrace(System.err); return null;}).get();

    }



    private void runEachToolsOnEachFiles(List<Tool> filteredTools, List<Path> filePaths) throws IOException {
        removeDirByNames(WORKINGPATH, filteredTools);
        for(Tool filteredTool : filteredTools) {
            for (Path filePath : filePaths) {
                String[] tempTokens = new String[]{ filteredTool.getCompilerNameFromTool(), filteredTool.getPath(), };
                String[] tokens = Stream.concat(Arrays.stream(tempTokens), Arrays.stream(String.format(filteredTool.getArguments(), filePath).split(" ")))
                        .toArray(String[]::new);
                byte[] toolResult = new ProcessHandler().getToolsResult(tokens);
                Path toolResultDir = writeToolResultToDir(WORKINGPATH, filteredTool, filePath.getFileName().toString(), toolResult);
                executeConversion(filteredTool.getName(), toolResultDir);
            }
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

    public CompletableFuture<?> executeConversion(String toolName, Path dir) {
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

    public Path writeToolResultToDir(String path, Tool tool, String fileName, byte[] result) throws IOException {
        FileHelper fileHelper = new FileHelper();
        Path dir = fileHelper.createDirectoryFromName(path, tool.getName());
        fileHelper.writeBytesIntoNewDir(path, tool.getName() + "/" + fileName.split("\\.")[0] + ".cgtxt", result);
        return dir;
    }

}
