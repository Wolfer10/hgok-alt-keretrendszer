package com.hgok.webapp.util;

import java.util.List;
import java.util.Set;

public interface FunctionNameExtractor {

    String extractFunctionName(String functionHeader);
    Set<String> extractFunctionNamesFromBody(String functionBody);
}
