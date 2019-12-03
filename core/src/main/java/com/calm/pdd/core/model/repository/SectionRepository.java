package com.calm.pdd.core.model.repository;

import com.calm.pdd.core.model.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
	List<Section> findByOrderBySectionOrderAsc();
}
