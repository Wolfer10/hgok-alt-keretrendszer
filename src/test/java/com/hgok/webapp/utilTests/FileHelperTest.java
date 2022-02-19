package com.hgok.webapp.utilTests;

import com.hgok.webapp.util.FileHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileHelperTest {


    public static final String TESTDIR = "src/test/java/com/hgok/webapp/utilTests/";
    public final String WORKINGPATH = "src/main/resources/static/working-dir/";


    @Test
    public void testCreateDir() throws IOException {
        FileHelper fileHelper = new FileHelper();
        Path path = fileHelper.createDirectoryFromName(TESTDIR, "Alma");
        Assertions.assertTrue(path.toFile().exists());
    }

    @Test
    public void testCreateDirIfExits() throws IOException {
        FileHelper fileHelper = new FileHelper();
        Files.createDirectories(Path.of(TESTDIR, "Alma"));
        Path path = fileHelper.createDirectoryFromName(TESTDIR, "Alma");
        Assertions.assertTrue(path.toFile().exists());
    }

    @Test
    public void testWriteResultToNewFile() throws IOException {
        FileHelper fileHelper = new FileHelper();
        String expected = "alma";
        Path path = fileHelper.writeBytesIntoNewDir(TESTDIR, "elem", expected.getBytes());
        Assertions.assertTrue(path.toFile().exists());
        AssertFromFile(path, expected);
    }

    @Test
    public void testWriteResultToNewDirAndFileExits() throws IOException {
        FileHelper fileHelper = new FileHelper();
        String expected = "alma";
        Path path1 = fileHelper.createDirectoryFromName(TESTDIR, "JANOS");
        Path path2 = fileHelper.writeBytesIntoNewDir(TESTDIR, "JANOS/elem2", expected.getBytes());
        Assertions.assertTrue(path1.toFile().exists());
        Assertions.assertTrue(path2.toFile().exists());
        AssertFromFile(path2, expected);
    }

    private void AssertFromFile(Path path2, String expected) throws IOException {
        String read = Files.readAllLines(path2).get(0);
        Assertions.assertEquals(expected, read);
    }


    @Test
    public void testWriteNewFileEveryTime() throws IOException {

        Assertions.assertTrue(Paths.get(TESTDIR).toFile().isDirectory());
        Assertions.assertTrue(Paths.get(TESTDIR).toFile().exists());

        if(Paths.get(TESTDIR + "alma.txt").toFile().exists()){
            Files.delete(Paths.get(TESTDIR + "alma.txt"));
        }

        Assertions.assertFalse(Paths.get(TESTDIR + "alma.txt").toFile().exists());

        Files.write(Paths.get(TESTDIR + "alma.txt"), "alma".getBytes());
    }

    @Test
    public void testWriteFileWithoutDirectory() throws IOException {
        Assertions.assertTrue(Paths.get(TESTDIR).toFile().isDirectory());
        Assertions.assertTrue(Paths.get(TESTDIR).toFile().exists());

        Path tempDirPath = Files.createDirectories(Paths.get(TESTDIR + "ALMA"));
        Assertions.assertTrue(tempDirPath.toFile().exists());

        if(Paths.get(TESTDIR+ "ALMA/alma.txt").toFile().exists()){
            Files.delete(Paths.get(TESTDIR + "ALMA/alma.txt"));
        }
        Assertions.assertFalse(Paths.get(TESTDIR + "ALMA/alma.txt").toFile().exists());

        Path tempPath= Files.write(Paths.get(TESTDIR + "ALMA/alma.txt"), "alma".getBytes());
        Assertions.assertTrue(tempPath.toFile().exists());
    }


}
