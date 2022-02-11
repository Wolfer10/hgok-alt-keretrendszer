package com.hgok.webapp.utilTests;

import com.hgok.webapp.util.FileHelper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelperTest {


    public static final String TESTDIR = "src/test/java/com/hgok/webapp/utilTests/";
    public final String WORKINGPATH = "src/main/resources/static/working-dir/";


    @Test
    public void testCreateDir() throws IOException {
        FileHelper fileHelper = new FileHelper();
        Path path = fileHelper.createDirectoryFromName(TESTDIR, "Alma");
        Assert.assertTrue(path.toFile().exists());
    }

    @Test
    public void testWriteResultToNewFile() throws IOException {
        FileHelper fileHelper = new FileHelper();
        Path path = fileHelper.writeBytesIntoNewDir(TESTDIR, "kakak", "satan".getBytes());
        Assert.assertTrue(path.toFile().exists());
    }

    @Test
    public void testWriteResultToNewDirAndFile() throws IOException {
        FileHelper fileHelper = new FileHelper();
        Path path1 = fileHelper.createDirectoryFromName(TESTDIR, "JANOS");
        Path path2 = fileHelper.writeBytesIntoNewDir(TESTDIR, "JANOS/kakak", "satan".getBytes());
        Assert.assertTrue(path1.toFile().exists());
        Assert.assertTrue(path2.toFile().exists());
    }



    @Test
    public void testWriteNewFileEveryTime() throws IOException {

        Assert.assertTrue(Paths.get(TESTDIR).toFile().isDirectory());
        Assert.assertTrue(Paths.get(TESTDIR).toFile().exists());

        if(Paths.get(TESTDIR + "alma.txt").toFile().exists()){
            Files.delete(Paths.get(TESTDIR + "alma.txt"));
        }

        Assert.assertFalse( Paths.get(TESTDIR + "alma.txt").toFile().exists());

        Files.write(Paths.get(TESTDIR + "alma.txt"), "alma".getBytes());
    }

    @Test
    public void testWriteFileWithoutDirectory() throws IOException {
        Assert.assertTrue(Paths.get(TESTDIR).toFile().isDirectory());
        Assert.assertTrue(Paths.get(TESTDIR).toFile().exists());

        Path tempDirPath = Files.createDirectories(Paths.get(TESTDIR + "ALMA"));
        Assert.assertTrue(tempDirPath.toFile().exists());

        if(Paths.get(TESTDIR+ "ALMA/alma.txt").toFile().exists()){
            Files.delete(Paths.get(TESTDIR + "ALMA/alma.txt"));
        }
        Assert.assertFalse( Paths.get(TESTDIR + "ALMA/alma.txt").toFile().exists());

        Path tempPath= Files.write(Paths.get(TESTDIR + "ALMA/alma.txt"), "alma".getBytes());
        Assert.assertTrue(tempPath.toFile().exists());
    }


}
