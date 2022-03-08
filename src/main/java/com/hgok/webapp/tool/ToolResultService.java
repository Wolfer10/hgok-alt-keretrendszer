package com.hgok.webapp.tool;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolResultService {

    @Autowired
    ToolResultRepository toolResultRepository;

    public ToolResult getToolResultByToolId(Long toolId, Long analysisId){
        if(toolId == null){
            throw new IllegalArgumentException();
        }
        return toolResultRepository.getToolResultByToolIdAndAnalysisId(toolId, analysisId);
    }

}
