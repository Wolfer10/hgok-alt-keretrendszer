package com.hgok.webapp.util;

public class JsFunctionExtractor implements FunctionExtractor {

    @Override
    public int extractFunctionEnd(String functionBody, int from) {
        int sumOfBraces = 0;
        for (int i = 0; i < functionBody.length(); i++) {
            if (functionBody.charAt(i) == '{') {
                sumOfBraces++;
            } else if (functionBody.charAt(i) == '}') {
                sumOfBraces--;
            }
            if(sumOfBraces == 0 && i > functionBody.indexOf('{', from)){
                return i;
            }
        }
        return sumOfBraces;
    }
}
