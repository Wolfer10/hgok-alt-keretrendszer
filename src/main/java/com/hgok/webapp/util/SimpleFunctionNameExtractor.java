package com.hgok.webapp.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleFunctionNameExtractor implements FunctionNameExtractor{

    @Override
    public String extractFunctionName(String functionHeader){
        Matcher matcher = getFunctionNameMatcher(functionHeader);
        boolean matchFound = isMatchFound(matcher);
        if(matchFound) {
           return matcher.group();
        }
        return "Match not found";
    }

    private Matcher getFunctionNameMatcher(String functionHeader) {
        Pattern pattern = Pattern.compile("((?<= )|(?<=))([A-Za-z.]+)((?=\\()|(?= \\())", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(functionHeader);
    }

    private boolean isMatchFound(Matcher matcher) {
        return matcher.find();
    }

    @Override
    public Set<String> extractFunctionNamesFromBody(String functionBody) {
        Matcher matcher = getFunctionNameMatcher(functionBody);
        boolean matchFound = isMatchFound(matcher);
        if(matchFound) {
            return matcher.results().map(MatchResult::group).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }
}
