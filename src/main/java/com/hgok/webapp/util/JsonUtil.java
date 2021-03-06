package com.hgok.webapp.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.tool.Tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JsonUtil {


    public static void dumpToolNamesIntoJson(List<Tool> tools, String fullpath) throws IOException {
        FileHelper fileHelper = new FileHelper();
        Map<String, String> nameMap = tools.stream().collect(Collectors.toMap(Tool::getName, Tool::getName));
        String toolNameJson = mapToJson(nameMap);
        fileHelper.writeBytesIntoNewFile(fullpath, "/tool-names.json", toolNameJson.getBytes());
    }

    /**
     * Beolvas egy jsont
     * Gson segítségével átkonvertálja convertedObject-é
     */
    public static ComparedAnalysis getComparedToolsFromJson(String filePath) throws FileNotFoundException {
        String content = new Scanner(new File(filePath))
                .useDelimiter("\\Z").next();

        Gson g = new Gson();
        JsonObject convertedObject = new Gson().fromJson(content, JsonObject.class);
        return g.fromJson(convertedObject, ComparedAnalysis.class);
    }

    public static <T> String mapToJson(Map<T, T> map){
        Gson g = new Gson();
        Type listType = new TypeToken<Map<String, String>>() {}.getType();
        return g.toJson(map, listType);
    }

}
