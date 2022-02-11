package com.hgok.webapp.utilTests;

import com.hgok.webapp.util.NtoMReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class NtoMReaderTests {

    @Test
    public void NtoEndreaderTest(){
        NtoMReader reader = new NtoMReader("F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\utilTests\\TestFile.js");
        String expected = "function bar(){\r\n" +
                "  var asd;\r\n" +
                "  var asd;\r\n" +
                "  fo();\r\n" +
                "}";
        String result = reader.readFromNToEnd(5);
        assertEquals(expected, result);
    }

}
