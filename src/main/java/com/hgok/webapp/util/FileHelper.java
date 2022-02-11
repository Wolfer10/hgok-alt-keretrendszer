package com.hgok.webapp.util;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {


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

    private void deleteFileIfExits(Path fullPath) throws IOException {
        if (fullPath.toFile().exists() && fullPath.toFile().isDirectory()) {
            FileUtils.deleteDirectory(fullPath.toFile());
        } else if(fullPath.toFile().exists()) {
            Files.delete(fullPath);
        }
    }

}
