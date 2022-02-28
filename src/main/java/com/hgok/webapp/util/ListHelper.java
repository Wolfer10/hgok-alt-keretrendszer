package com.hgok.webapp.util;

import com.hgok.webapp.compared.ComparedAnalysis;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public  class ListHelper   {

    public static <T> List<T> flatMap(Stream<List<T>> stream) {
        return stream.flatMap(Collection::stream).collect(Collectors.toList());
    }
}
