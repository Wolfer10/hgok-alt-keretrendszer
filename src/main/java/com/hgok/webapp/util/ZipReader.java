package com.hgok.webapp.util;

import net.lingala.zip4j.ZipFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipReader {

    public static final int BUFFER_SIZE = 4096;

    public static void unzip(Path source, Path target) throws IOException {
        new ZipFile(source.toFile())
                .extractAll(target.toString());

    }

}
