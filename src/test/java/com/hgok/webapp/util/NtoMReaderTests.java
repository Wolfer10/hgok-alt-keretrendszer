package com.hgok.webapp.util;

import com.hgok.webapp.util.NtoMReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class NtoMReaderTests {

    @Test
    public void NtoEndreaderTest(){
        NtoMReader reader = new NtoMReader("F:\\Feri\\egyetem\\szakdoga\\hgok-alt-keretrendszer\\src\\test\\java\\com\\hgok\\webapp\\util\\TestFile.js");
        String expected = "function bar(){\n" +
                "    let asd = \"foo\";\n" +
                "    foo(asd);\n" +
                "}\n" +
                "\n" +
                "function main(){\n" +
                "    let asd = \"foo\";\n" +
                "    bar();\n" +
                "    foo(asd)\n" +
                "}\n";
        String result = reader.readFromNToEnd(5);
        assertEquals(expected, result);
    }

}
