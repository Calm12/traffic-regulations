package com.calm.pdd.core.model.session;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class QuestionProgressFactoryTest {
	
	private List<Question> questions;
	
	private QuestionProgressFactory questionProgressFactory;
	
	@BeforeEach
	public void setUp() {
		questions = Arrays.asList(
				new Question().setId(54).setNumber(4).setSection(new Section(1, "1", "section_1")),
				new Question().setId(52).setNumber(2),
				new Question().setId(53).setNumber(3),
				new Question().setId(51).setNumber(1)
		);
		
		questionProgressFactory = new QuestionProgressFactory();
	}
	
	@Test
	void buildWithFixedSection() {
		QuestionProgress progress = questionProgressFactory.buildProgressWithFixedSection(questions, 1);
		
		assertThat(progress.getSectionId()).isEqualTo(1);
		assertThat(progress.getProgressName()).isEqualTo("1. section_1");
		assertThat(progress.getSectionType()).isEqualTo(SectionType.FIXED);
		
		assertThat(progress.getList().size()).isEqualTo(4);
		
		assertThat(progress.getList().get(0)).hasFieldOrPropertyWithValue("questionId", 54).hasFieldOrPropertyWithValue("questionNumber", 4);
		assertThat(progress.getList().get(3)).hasFieldOrPropertyWithValue("questionId", 51).hasFieldOrPropertyWithValue("questionNumber", 1);
	}
	
	@Test
	void buildWithRandomSet() {
		QuestionProgress progress = questionProgressFactory.buildProgressWithRandomQuestions(questions);
		
		assertThat(progress.getSectionId()).isEqualTo(0);
		assertThat(progress.getProgressName()).isEqualTo("20 случайных вопросов");
		assertThat(progress.getSectionType()).isEqualTo(SectionType.RANDOM);
		
		assertThat(progress.getList().size()).isEqualTo(4);
		
		assertThat(progress.getList().get(0)).hasFieldOrPropertyWithValue("questionId", 54).hasFieldOrPropertyWithValue("questionNumber", 1);
		assertThat(progress.getList().get(3)).hasFieldOrPropertyWithValue("questionId", 51).hasFieldOrPropertyWithValue("questionNumber", 4);
	}
}
