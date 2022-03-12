package com.hgok.webapp.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

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

    public Path writeBytesIntoNewFile(String path, String fileName, byte[] result) throws IOException {
        Path fullPath = Path.of(path , fileName);
        return Files.write(fullPath, result);
    }


    public static String getRelativeName(String filename){
        return filename.split("/")[filename.split("/").length - 2] + "/" + filename.split("/")[filename.split("/").length - 1];
    }


    public void deleteFileIfExits(Path fullPath) throws IOException {
        if (fullPath.toFile().exists() && fullPath.toFile().isDirectory()) {
            FileUtils.deleteDirectory(fullPath.toFile());
        } else if(fullPath.toFile().exists()) {
            Files.delete(fullPath);
        }
    }
    public List<Path> getFilePaths() throws IOException {
        List<Path> filePaths = new ArrayList<>();
        if ("zip".equals(filePath.getFileName().toString().split("\\.")[1])){
            filePaths = getFilePathsFromZip();
        } else {
            filePaths.add(filePath);
        }
        return filePaths;
    }

    private List<Path> getFilePathsFromZip() throws IOException {
        List<Path> filePaths;
        ZipReader.unzip(getFilePath(), Path.of(FileHelper.SOURCE_FOLDER));
        File dir = new File(FileHelper.SOURCE_FOLDER);
        filePaths = getFilesFromDir(dir);
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

    public void removeDirByName(String path, String name) throws IOException {
        deleteFileIfExits(Path.of(path, name));
    }

    public void appendToFile(Path filePath, byte[] toolResult) throws IOException {
        try (FileWriter fileWriter = new FileWriter(String.valueOf(filePath), true)){
            fileWriter.write(new String(toolResult));
        }
    }

    public Path createDirAndInsertFile(Path path, String fileName, String ext) throws IOException {
        return getPath(path, fileName, fileName, ext);
    }

    public Path createDirAndInsertFile(Path path, String dirName, String fileName, String ext) throws IOException {
        return getPath(path, dirName, fileName, ext);
    }


    private Path getPath(Path path, String dirName, String fileName, String ext) throws IOException {
        Path dir = createDirectoryFromName(String.valueOf(path), dirName);
        if(new File(String.valueOf(Path.of(String.valueOf(dir), fileName + ext))).exists()){
            return Path.of(String.valueOf(dir), fileName + ext);
        }
        return Files.createFile(Path.of(String.valueOf(dir), fileName + ext));
    }


}
