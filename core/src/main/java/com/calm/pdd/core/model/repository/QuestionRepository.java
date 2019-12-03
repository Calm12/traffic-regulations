package com.calm.pdd.core.model.repository;

import com.calm.pdd.core.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
	List<Question> getListBySectionId(int section);
}
