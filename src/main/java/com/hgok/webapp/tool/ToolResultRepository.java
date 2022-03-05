package com.hgok.webapp.tool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ToolResultRepository extends JpaRepository<ToolResult, Long> {

    @Query(value = "SELECT tresult FROM ToolResult tresult, Tool tool WHERE :id = tool.id WHERE tool.id = tresult.id",
            nativeQuery=true)
    List<ToolResult> getToolResultByMetricContainerId(@Param(value = "id") Long id);
}
