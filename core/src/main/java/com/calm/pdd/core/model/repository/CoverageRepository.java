package com.calm.pdd.core.model.repository;

import com.calm.pdd.core.model.entity.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverageRepository extends JpaRepository<Coverage, Integer> {
	
}
