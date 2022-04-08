package com.hgok.webapp.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ZipReaderTest {

    public static String UTIL_ZIP = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\util.zip";
    public static String EXAMPLE_ZIP = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\example.zip";
    public static String UNFORMATTED = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\unformatted.zip";
    public static String MATH = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\mathjs.zip";
    public static String MATH2 = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\math.zip";
    public static String DEST_FOLDER = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\destDir";
    public static String DEST_FOLDER2 = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\destDir2";
    public static String DEST_FOLDER3 = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\destDir3";
    public static String DEST_FOLDER4 = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\destDir4";

    @Test
    public void testUnzip() throws IOException {
        ZipReader.unzip(Path.of(UTIL_ZIP), Path.of(DEST_FOLDER));
        Path path2 = Path.of(DEST_FOLDER, "TestFile2.js");
        Path path3 = Path.of(DEST_FOLDER, "TestFile3.js");
        assertThat(path2).exists();
        assertThat(path3).exists();


    }

    @Test
    public void testUnzipWithFolders() throws IOException {
        ZipReader.unzip(Path.of(EXAMPLE_ZIP), Path.of(DEST_FOLDER2));
        Path path2 = Path.of(DEST_FOLDER2, "example-repo");
        assertThat(path2).exists().isDirectory();
    }

    @Test
    public void testUnzipWithUnknownFormats() throws IOException {
        ZipReader.unzip(Path.of(UNFORMATTED), Path.of(DEST_FOLDER3));
        Path path2 = Path.of(DEST_FOLDER3, "unformatted");
        assertThat(path2).exists();
    }


    public void testUnZipMathJs() throws IOException {
        ZipReader.unzip(Path.of(MATH2), Path.of(DEST_FOLDER4));
//        Path path2 = Path.of(DEST_FOLDER4, "unformatted");
//        assertThat(path2).exists();
    }

}