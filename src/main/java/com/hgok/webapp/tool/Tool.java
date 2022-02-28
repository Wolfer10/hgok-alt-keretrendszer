package com.hgok.webapp.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Locale;

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
}
