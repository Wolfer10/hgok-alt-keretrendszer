package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends CrudRepository<Analysis, Long> {


}
