package com.hgok.webapp.analysis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisRepository extends CrudRepository<Analysis, Long> {

}
