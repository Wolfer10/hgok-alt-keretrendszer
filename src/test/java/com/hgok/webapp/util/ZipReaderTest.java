package com.hgok.webapp.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ZipReaderTest {

    public static String UTIL_ZIP = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\util.zip";
    public static String DEST_FOLDER = "F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\destDir";

    @Test
    public void testUnzip() throws IOException {
        ZipReader zipReader = new ZipReader();
        zipReader.unzip(UTIL_ZIP,DEST_FOLDER);
        Path path2 = Path.of(DEST_FOLDER, "TestFile2.js");
        Path path3 = Path.of(DEST_FOLDER, "TestFile3.js");
        assertThat(path2).exists();
        assertThat(path3).exists();


    }

}