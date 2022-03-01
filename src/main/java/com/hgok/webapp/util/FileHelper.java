package com.hgok.webapp.util;

import com.hgok.webapp.tool.Tool;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hgok.webapp.analysis.AnalysisService.WORKINGPATH;

@Getter
@Setter
public class FileHelper {

    public static final String SOURCE_FOLDER = "src/main/resources/static/working-dir/sourceFiles";
    public static final String COMPARED_FOLDER = "src/main/resources/static/working-dir/x-compared";

    private Path filePath;


    public Path createDirectoryFromName(String path, String name) throws IOException {
        Path fullPath = Path.of(path, name);
        Files.createDirectories(fullPath);
        return fullPath;
    }

    public Path writeBytesIntoNewDir(String path, String fileName, byte[] result) throws IOException {
        Path fullPath = Paths.get(path + fileName);
        return Files.write(fullPath, result);
    }

    public void deleteFileIfExits(Path fullPath) throws IOException {
        if (fullPath.toFile().exists() && fullPath.toFile().isDirectory()) {
            FileUtils.deleteDirectory(fullPath.toFile());
        } else if(fullPath.toFile().exists()) {
            Files.delete(fullPath);
        }
    }

    public void saveFile(String folder, MultipartFile multipartFile) throws IOException {
        Path path =  Paths.get(folder, File.separator , multipartFile.getOriginalFilename());
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        filePath = path;
    }

    public String replaceFormat(String filename, String newFormat){
        return filename.split("\\.")[0] + newFormat;
    }

    public List<Path> getPaths() throws IOException {
        List<Path> filePaths = new ArrayList<>();
        if ("zip".equals(getFilePath().getFileName().toString().split("\\.")[1])){
            ZipReader zipReader = new ZipReader();
            zipReader.unzip(getFilePath().toString(), FileHelper.SOURCE_FOLDER);
            File dir = new File(FileHelper.SOURCE_FOLDER);
            filePaths = getFilesFromDir(dir);
        } else {
            filePaths.add(getFilePath());
        }
        return filePaths;
    }

    public List<Path> getFilesFromDir(File dir) {
        List<Path> filePaths;
        filePaths = FileUtils.listFiles(dir, new String[] {"js"}, true)
                .stream()
                .map(File::getPath)
                .map(Path::of)
                .collect(Collectors.toList());
        return filePaths;
    }
    public static void removeDirByNames(String path, List<Tool> filteredTools) throws IOException {
        for(Tool filteredTool : filteredTools) {
            FileHelper fileHelper = new FileHelper();
            fileHelper.deleteFileIfExits(Path.of(path, filteredTool.getName()));
        }
    }

}
