package com.hgok.webapp.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tool")
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    @NotBlank
    private String name;

    @NotBlank
    private String language;

    @NotBlank
    private String path;

    @NotBlank
    private String arguments;

    private String environmentArgs;

    public void copyTool(Tool tool){
        setName(tool.getName());
        setLanguage(tool.getLanguage());
        setPath(tool.getPath());
        setArguments(tool.getArguments());
        setEnvironmentArgs(tool.getEnvironmentArgs());
    }

    public Tool(@NotBlank String name) {
        this.name = name;
    }

    public String getCompilerNameFromTool() {
        String compilerName = "";
        if(language.toLowerCase(Locale.ROOT).contains("javascript")){
            compilerName = "node";
        }
        return compilerName;
    }

    public String[] generateTokensFromFilePath(Path filePath) {
        String[] tempTokens = new String[]{ getCompilerNameFromTool(), getPath() };
        return Stream.concat(Arrays.stream(tempTokens), Arrays.stream(String.format(getArguments(), filePath).split(" ")))
                .toArray(String[]::new);
    }

    public String[] generateTokensFromFilePaths(List<Path> filePaths) {
        String[] tempTokens = new String[]{ getCompilerNameFromTool(), getPath() };
        return Stream.concat(Arrays.stream(tempTokens), Arrays.stream(String.format(getArguments(), StringUtils.join(filePaths, " ")).split(" ")))
                .toArray(String[]::new);
    }

}
