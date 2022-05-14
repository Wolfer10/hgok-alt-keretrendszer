package com.hgok.webapp.tool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {

    @Query(value = "SELECT DISTINCT t.language  FROM tool t",
            nativeQuery=true)
    List<String> groupToolLanguages();

    @Query(value = "SELECT * FROM Tool t WHERE t.language = :ln",
            nativeQuery=true)
    List<Tool> getToolsFromLanguage(@Param("ln") String language);

    @Query(value = "SELECT * FROM Tool t WHERE t.id = :id",
            nativeQuery=true)
    List<Tool> getToolsByAnalysisId(@Param("id") Long id);

}