package com.hgok.webapp.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class JsFunctionExtractorTest {

    @Test
    void extractSimpleFunctionTest(){
        int expected = 4;
        FunctionExtractor extractor = new JsFunctionExtractor();
        String jsFunction = "a(){}";
        assertThat(extractor.extractFunctionEnd(jsFunction, 0)).isEqualTo(expected);
    }

    @Test
    void extractSimpleFunctionWithMoreLineTest(){
        int expected = 16;
        FunctionExtractor extractor = new JsFunctionExtractor();
        String jsFunction = "a(){let alma = 1}\n let alma = 0;";
        assertThat(extractor.extractFunctionEnd(jsFunction, 0)).isEqualTo(expected);
    }

    @Test
    void extractSimpleFunctionWithInsideBracesTest(){
        int expected = 29;
        FunctionExtractor extractor = new JsFunctionExtractor();
        String jsFunction = "a(){ if(true){}\n let alma = 1}\n let alma = 0;";
        assertThat(extractor.extractFunctionEnd(jsFunction, 0)).isEqualTo(expected);
    }

}