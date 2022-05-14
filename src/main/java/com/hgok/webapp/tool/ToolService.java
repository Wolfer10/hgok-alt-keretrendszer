package com.hgok.webapp.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToolService {

    ToolRepository toolRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public List<String> getToolNamesFromLanguage(String language){
        return toolRepository.getToolsFromLanguage(language).stream()
                .map(Tool::getName)
                .collect(Collectors.toList());
    }

    public List<String> groupToolLanguages() {
        return toolRepository.groupToolLanguages();
    }

    public void save(Tool tool){
        toolRepository.save(tool);
    }

    public Tool findById(Long id){
       return toolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tool Id:" + id));
    }

    public List<Tool> findAll(){
        return toolRepository.findAll();
    }

    public void delete(Tool tool){
        toolRepository.delete(tool);
    }


}
