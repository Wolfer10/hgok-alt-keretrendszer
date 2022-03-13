package com.hgok.webapp.compared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {


    @Query(value = "SELECT found_by FROM link_found_by WHERE link_id =:id",
            nativeQuery=true)
    List<String> getFoundBysByLink(@Param("id") Long id);
}