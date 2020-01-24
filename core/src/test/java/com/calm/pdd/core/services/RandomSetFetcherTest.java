package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RandomSetFetcherTest {
	
	@Mock
	private QuestionRepository questionRepository;
	
	@Mock
	private QuestionProgressFactory questionProgressFactory;
	
	@Mock
	private QuestionProgress questionProgress;
	
	@Mock
	private Question question;
	
	@BeforeEach
	public void setUp() {
		List<Question> questionsList = Collections.singletonList(question);
		
		when(questionRepository.getRandomList(20)).thenReturn(questionsList);
		when(questionProgressFactory.buildProgressWithRandomQuestions(questionsList)).thenReturn(questionProgress);
	}
	
	@Test
	void successFetchQuestionsSet() {
		RandomSetFetcher randomSetFetcher = new RandomSetFetcher(questionRepository, questionProgressFactory);
		
		QuestionProgress fetchedQuestionProgress = randomSetFetcher.fetchSet();
		assertThat(fetchedQuestionProgress).isEqualTo(questionProgress);
	}
}
