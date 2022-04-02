package com.hgok.webapp.analysis;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends CrudRepository<Analysis, Long> {

    @EntityGraph(attributePaths = "tools")
    Analysis findAnalysisById(Long Id);

    @Query(value = "SELECT a FROM Analysis a join fetch a.comparedAnalysis c")
    List<Analysis> findAllAnalysisWithComparedAndTool();


}
