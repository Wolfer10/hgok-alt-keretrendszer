package com.hgok.webapp.tool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolResultRepository extends JpaRepository<ToolResult, Long> {

    @Query(value = "SELECT * FROM tool_result WHERE :tool_id = tool_result.tool_id AND :analysis_id = tool_result.analysis_id ",
            nativeQuery=true)
    ToolResult getToolResultByToolIdAndAnalysisId(@Param(value = "tool_id") Long toolId, @Param(value = "analysis_id") Long analysId);
}
