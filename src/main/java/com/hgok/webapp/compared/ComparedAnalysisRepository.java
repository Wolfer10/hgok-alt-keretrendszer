package com.hgok.webapp.compared;

import com.hgok.webapp.analysis.Analysis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComparedAnalysisRepository extends CrudRepository<ComparedAnalysis, Long> {

    List<ComparedAnalysis> findAllByAnalysis_Id(Long ID);
}
