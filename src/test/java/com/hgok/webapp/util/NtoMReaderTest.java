package com.hgok.webapp.util;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


public class NtoMReaderTest {

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
        String result = reader.readFromNtoEnd(5);
        assertEquals(expected, result);
    }


    @Test
    void readFromNLineToMCharacter()  {
        NtoMReader reader = new NtoMReader();
        String source =
                "function bar(){\n" +
                "    let asd = \"foo\";\n" +
                "    foo(asd);\n" +
                "}\n" +
                "\n" +
                "function main(){\n" +
                "    let asd = \"foo\";\n" +
                "    bar();\n" +
                "    foo(asd)\n" +
                "}\n";
        String expected = "function main(){\n" +
                "    let asd = \"foo\";\n" +
                "    bar();\n" +
                "    foo(asd)\n" +
                "}\n";
        try(BufferedReader br = new BufferedReader(new StringReader(source))){
            String result = reader.readFromNLineToMCharacter(br, 5, 68);
            assertEquals(expected, result);
        }  catch (IOException e){
            e.printStackTrace();
        }
    }


    @Test
    void getStringNthLine() {
        NtoMReader reader = new NtoMReader();
        String source =
                "function bar(){\n" +
                        "    let asd = \"foo\";\n" +
                        "    foo(asd);\n" +
                        "}\n" +
                        "\n" +
                        "function main(){\n" +
                        "    let asd = \"foo\";\n" +
                        "    bar();\n" +
                        "    foo(asd)\n" +
                        "}\n";
        assertThat(reader.getStringNthLine(source,1)).isEqualTo("function bar(){");
        assertThat(reader.getStringNthLine(source, 6)).isEqualTo("function main(){");
    }
}
