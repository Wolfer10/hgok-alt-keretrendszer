package com.hgok.webapp.analysis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hgok.webapp.compared.*;
import com.hgok.webapp.tool.Tool;
import com.hgok.webapp.tool.ToolRepository;
import com.hgok.webapp.util.NtoMReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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


    public void startAnalysis(String[] toolNames) throws IOException {
        List<Tool> filteredTools = new ArrayList<>();
        List<Tool> tools = toolRepository.findAll();
        for (String name : toolNames) {
            filteredTools.addAll(tools.stream().filter(tool -> tool.getName().equals(name)).collect(Collectors.toList()));
        }

        Analysis analysis = new Analysis(tools, "folyamatban", new Timestamp(System.currentTimeMillis()));

        analysisRepository.save(analysis);

        filteredTools.forEach(filteredTool -> startProcess(filteredTool.getArguments(), filteredTool.getName(), analysis));

        Exocutor.addToQueue(() -> {
            try {
                compareAnalysises(WORKINGPATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ComparedTools comparedTools = getComparedToolsFromJson();

        initLinks(analysis, comparedTools);

        List<Label> labels = new ArrayList<>();
        analysis.getLinks().forEach(link -> labels.add(new Label(link.getLabel(), link)));

        labels.forEach(label -> {
            NtoMReader ntoMSourceReader = new NtoMReader(label.getSourceFileName());
            NtoMReader ntoMTargetReader = new NtoMReader(label.getTargetFileName());
            label.getLink().setSourceSnippet(ntoMSourceReader.readFromNToEnd(label.getSourceStartLine()));
            label.getLink().setTargetSnippet(ntoMTargetReader.readFromNToEnd(label.getTargetStartLine()));
        });
        Analysis newAnalysis = analysisRepository.findById(analysis.getId())
                .orElseThrow(() -> new EntityNotFoundException(analysis.getId().toString()));
        newAnalysis.setStatus("kész");
        analysisRepository.save(newAnalysis);
    }


    /**
     * Beolvas egy jsont
     * Gson segítségével átkonvertálja convertedObject-é
     */
    private ComparedTools getComparedToolsFromJson() throws FileNotFoundException {
        // TODO ez égetve lett call-graph helyett a file neve lesz
        String content = new Scanner(new File("src/main/resources/static/working-dir/x-compared/call-graph.json"))
                .useDelimiter("\\Z").next();

        Gson g = new Gson();
        JsonObject convertedObject = new Gson().fromJson(content, JsonObject.class);
        return g.fromJson(convertedObject, ComparedTools.class);
    }

    public void initLinks(Analysis analysis, ComparedTools comparedTools) {
        comparedTools.links.forEach(analysis::addLink);
    }


    public void startProcess(String command, String name, Analysis analysis) {

        String[] tokens = command.split(" ");

        /**
         *  Ezek csak a toolonkénti egy input (utána kellen több inputra is)
         *  Kellene külön az elemzésre is (jelenleg oda írom ki kétszer)
         */
        Exocutor.addToQueue(() -> {
            try {
                log.error(name + " kezdete");
                Path toolWorkDirPath = Paths.get(WORKINGPATH + name);
                Files.createDirectories(toolWorkDirPath);
                ProcessBuilder toolProcessBuilder = new ProcessBuilder(tokens);
                Process rawAnalysis = toolProcessBuilder.start();
                byte[] result = rawAnalysis.getInputStream().readAllBytes();
                //System.out.println(Arrays.toString(result));
                // ez égetve lett call-graph helyett a file neve lesz
                Path rawCgPath = Paths.get(WORKINGPATH + name + "/" + "call-graph" + ".cgtxt");
                Files.write(rawCgPath, result);
                convertRawAnalysis(toolWorkDirPath.toString());
                log.error(name + "vége");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void compareAnalysises(String dir) throws IOException {
        startHCGCompare(dir);
    }

    private void convertRawAnalysis(String dir) throws IOException {
        startHCGScript(dir);
    }

    private void startHCGScript(String dir) throws IOException {
        ProcessBuilder convertProcessBuilder = new ProcessBuilder("python", "F:\\Feri\\egyetem\\szakdoga\\hcg-js-framework\\jscg_convert2json.py", dir);
        Process convertProcess = convertProcessBuilder.start();
        String convertResult = new String(convertProcess.getInputStream().readAllBytes());
        //System.out.println(convertResult);
    }

    private void startHCGCompare(String dir) throws IOException {
        ProcessBuilder convertProcessBuilder = new ProcessBuilder("python", "F:\\Feri\\egyetem\\szakdoga\\hcg-js-framework\\jscg_compare_json.py", dir, "noentry", "nowrapper");
        Process convertProcess = convertProcessBuilder.start();
        String convertResult = new String(convertProcess.getInputStream().readAllBytes());
        //System.out.println(convertResult);
    }
}
