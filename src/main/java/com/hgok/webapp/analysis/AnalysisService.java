package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.*;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolRepository;
import com.hgok.webapp.util.FileHelper;
import com.hgok.webapp.util.NtoMReader;
import com.hgok.webapp.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class AnalysisService {


    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    public final String WORKINGPATH = "src/main/resources/static/working-dir/";

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private LinkService linkService;

    public List<Analysis> getOrderedAnalysises(){
        return StreamSupport.stream(analysisRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Analysis::getTimestamp).reversed()).collect(Collectors.toList());
    }


    public void startAnalysis(String[] toolNames) throws IOException, InterruptedException {
        List<Tool> filteredTools = filterTools(toolNames);

        Analysis analysis = new Analysis(filteredTools, "folyamatban", new Timestamp(System.currentTimeMillis()));

        analysisRepository.save(analysis);

        filteredTools.forEach(filteredTool -> writeToolsResultToDirAndConvertIT(filteredTool.getArguments(), filteredTool.getName()));

        JsonUtil.dumpToolNamesIntoJson(filteredTools, WORKINGPATH);

        executeComparition(analysis).thenRun(() -> {
            try {
                analysisRepository.save(findAnalysisToUpdate(analysis));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });



    }


    private List<Tool> filterTools(String[] toolNames) {
        List<Tool> filteredTools = new ArrayList<>();
        List<Tool> tools = toolRepository.findAll();
        for (String name : toolNames) {
            filteredTools.addAll(tools.stream().filter(tool -> tool.getName().equals(name)).collect(Collectors.toList()));
        }
        return filteredTools;
    }

    private CompletableFuture<?> executeComparition(Analysis analysis) {
        return CompletableFuture.runAsync(() -> {
            try {
                startHCGCompare(WORKINGPATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, Exocutor.executor );
    }

    public Analysis findAnalysisToUpdate(Analysis analysis) throws FileNotFoundException {
        Analysis newAnalysis = analysisRepository.findById(analysis.getId())
                .orElseThrow(() -> new EntityNotFoundException(analysis.getId().toString()));
        newAnalysis.setStatus("kész");
        newAnalysis.setLinks(new ArrayList<>());
        intitLabels(newAnalysis);
       return newAnalysis;
    }

    public void  intitLabels(Analysis analysis) throws FileNotFoundException {
        log.error("INIT LABLES CALLED");
        JsonUtil jsonUtil = new JsonUtil();
        ComparedTools comparedTools = jsonUtil.getComparedToolsFromJson();
        log.error(String.valueOf(comparedTools.getLinks().size()));
        initLinks(analysis, comparedTools);
        log.error(String.valueOf(analysis.getLinks().size()));

        List<Label> labels = new ArrayList<>();
        analysis.getLinks().forEach(link -> labels.add(new Label(link.getLabel(), link)));

        labels.forEach(label -> {
            NtoMReader ntoMSourceReader = new NtoMReader(label.getSourceFileName());
            NtoMReader ntoMTargetReader = new NtoMReader(label.getTargetFileName());
            label.getLink().setSourceSnippet(ntoMSourceReader.readFromNToEnd(label.getSourceStartLine()));
            label.getLink().setTargetSnippet(ntoMTargetReader.readFromNToEnd(label.getTargetStartLine()));
        });
    }


    private void initLinks(Analysis analysis, ComparedTools comparedTools) {
        comparedTools.links.forEach(analysis::addLink);
    }


    public void writeToolsResultToDirAndConvertIT(String toolsCommand, String toolName) {
        // TODO
        //  Ezek csak a toolonkénti egy input (utána kellen több inputra is)
        //  Kellene külön az elemzésre is (jelenleg oda írom ki kétszer)
        Exocutor.addToQueue(() -> {
            try {
                log.error(toolName + " kezdete");
                FileHelper fileHelper = new FileHelper();
                Path dir = fileHelper.createDirectoryFromName(WORKINGPATH, toolName);
                fileHelper.writeBytesIntoNewDir(WORKINGPATH, toolName + "/" + "callgraph.cgtxt", getToolsResult(toolsCommand.split(" ")));
                startHCGConvert(dir);
                log.error(toolName + " vége");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    public byte[] getToolsResult(String[] tokens) throws IOException {
        ProcessBuilder toolProcessBuilder = new ProcessBuilder(tokens);
        Process rawAnalysis = toolProcessBuilder.start();
        System.err.println( new String(rawAnalysis.getErrorStream().readAllBytes()));
        return rawAnalysis.getInputStream().readAllBytes();

    }


    public void startHCGConvert(Path dir) throws IOException {
        ProcessBuilder convertProcessBuilder = new ProcessBuilder("python", "src/main/resources/hcg/jscg_convert2json.py", dir.toString());
        Process convertProcess = convertProcessBuilder.start();
        String convertResult = new String(convertProcess.getInputStream().readAllBytes());
        String error = new String(convertProcess.getErrorStream().readAllBytes());
//        System.err.println(error);
//        System.out.println(convertResult);
    }

    public void startHCGCompare(String dir) throws IOException {
        log.error("COMPARE CALLED");
        ProcessBuilder convertProcessBuilder = new ProcessBuilder("python", "src/main/resources/hcg/jscg_compare_json.py", dir, "noentry", "nowrapper");
        Process convertProcess = convertProcessBuilder.start();
        String result = new String(convertProcess.getInputStream().readAllBytes());
        String error = new String(convertProcess.getErrorStream().readAllBytes());
        log.error("COMPARE ENDED");

//        System.err.println(error);
//        System.out.println(result);
    }
}
