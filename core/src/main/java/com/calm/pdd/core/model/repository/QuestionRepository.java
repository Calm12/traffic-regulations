package com.calm.pdd.core.model.repository;

import com.calm.pdd.core.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
	List<Question> getListBySectionId(int section);
	
	@Query(nativeQuery = true, value = "SELECT * FROM questions ORDER BY rand() LIMIT :count")
	List<Question> getRandomList(@Param("count") int count);
}
