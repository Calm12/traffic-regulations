package com.calm.pdd.core.model.repository;

import com.calm.pdd.core.config.spring.PersistenceContext;
import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = PersistenceContext.class)
public class QuestionRepositoryTest {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private SectionRepository sectionRepository;
	
	@BeforeEach
	public void setUp() {
		Section firstSection = new Section(1, "1", "first");
		Section secondSection = new Section(2, "2", "second");
		
		sectionRepository.save(firstSection);
		sectionRepository.save(secondSection);
		
		questionRepository.saveAll(Arrays.asList(
				new Question().setId(51).setNumber(1).setSection(firstSection),
				new Question().setId(52).setNumber(2).setSection(firstSection),
				new Question().setId(53).setNumber(3).setSection(firstSection),
				new Question().setId(54).setNumber(4).setSection(firstSection),
				new Question().setId(55).setNumber(5).setSection(firstSection),
				new Question().setId(56).setNumber(6).setSection(secondSection),
				new Question().setId(57).setNumber(7).setSection(secondSection),
				new Question().setId(58).setNumber(8).setSection(secondSection),
				new Question().setId(59).setNumber(9).setSection(secondSection),
				new Question().setId(60).setNumber(10).setSection(secondSection)
		));
	}
	
	@Test
	void getRandomList() {
		List<Question> randomList1 = questionRepository.getRandomList(3);
		List<Question> randomList2 = questionRepository.getRandomList(3);
		
		assertThat(randomList1.size()).isEqualTo(3);
		assertThat(randomList2.size()).isEqualTo(3);
		assertThat(randomList1).isNotEqualTo(randomList2);
	}
	
	@Test
	void getFixedSectionList() {
		List<Question> list1 = questionRepository.getListBySectionId(1);
		List<Question> list2 = questionRepository.getListBySectionId(1);
		
		assertThat(list1.size()).isEqualTo(5);
		assertThat(list1.get(0).getSection().getId()).isEqualTo(1);
		assertThat(list1).isEqualTo(list2);
	}
}
