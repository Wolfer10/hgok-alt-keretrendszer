package com.hgok.webapp.util;

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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class FileHelper {

    public static final String SOURCE_FOLDER = "src/main/resources/static/working-dir/sourceFiles";
    public static final String COMPARED_FOLDER = "src/main/resources/static/working-dir/x-compared";

    private Path filePath;


    public Path createDirectoryFromName(String path, String name) throws IOException {
        Path fullPath = Path.of(path, name);
        deleteFileIfExits(fullPath);
        Files.createDirectories(fullPath);
        return fullPath;
    }

    public Path writeBytesIntoNewDir(String path, String fileName, byte[] result) throws IOException {
        Path fullPath = Paths.get(path + fileName);
        deleteFileIfExits(fullPath);
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

    public Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
    public String replaceFormat(String filename, String newFormat){
        return filename.split("\\.")[0] + newFormat;
    }

}
