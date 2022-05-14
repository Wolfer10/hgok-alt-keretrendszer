package com.hgok.webapp.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class SimpleFunctionNameExtractorTest {
    

    @Test
    void extractFunctionName() {
        SimpleFunctionNameExtractor extractor = new SimpleFunctionNameExtractor();
        String functionHeader = "function alma(){";
        assertEquals("alma",  extractor.extractFunctionName(functionHeader));
    }

    @Test
    void extractFunctionNameWithSpaceBetween() {
        SimpleFunctionNameExtractor extractor = new SimpleFunctionNameExtractor();
        String functionHeader = "function alma (){";
        assertEquals("alma",  extractor.extractFunctionName(functionHeader));
    }

    @Test
    void extractFunctionNamesFromFunction() {
        SimpleFunctionNameExtractor extractor = new SimpleFunctionNameExtractor();
        String function = "function main(){\n" +
                "    let asd = \"foo\";\n" +
                "    bar() "+
                "    bar();\n" +
                "    foo(asd)\n" +
                "}\n";;
        assertThat(extractor.extractFunctionNamesFromBody(function)).isEqualTo(Set.of("bar", "foo"));
    }

    @Test
    void extractFunctionNamesFromBody() {
        SimpleFunctionNameExtractor extractor = new SimpleFunctionNameExtractor();
        String function = "bar() => {\n" +
                "    let asd = \"foo\";\n" +
                "    console.log() "+
                "    bar ();\n" +
                "    foo (asd)\n" +
                "asd ( )"+
                "}\n";;
        assertThat(extractor.extractFunctionNamesFromBody(function)).isEqualTo(Set.of("asd", "console.log", "foo", "bar"));
    }

    @Test
    void extractFunctionNamesEmpty() {
        SimpleFunctionNameExtractor extractor = new SimpleFunctionNameExtractor();
        String function = "() => {}";
        assertThat(extractor.extractFunctionNamesFromBody(function)).isEqualTo(new HashSet<>());
    }

}