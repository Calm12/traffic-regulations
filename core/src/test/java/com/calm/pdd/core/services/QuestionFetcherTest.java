package com.calm.pdd.core.services;

import com.calm.pdd.core.exceptions.UnknownQuestionException;
import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class QuestionFetcherTest {
	
	@Mock
	private QuestionRepository questionRepository;
	
	@Mock
	private QuestionProgress questionProgress;
	
	@Mock
	private QuestionProgressUnit questionProgressUnit;
	
	@Mock
	private Question question;
	
	private QuestionFetcher questionFetcher;
	
	@BeforeEach
	public void setUp() {
		when(questionProgress.getByNumber(1)).thenReturn(questionProgressUnit);
		when(questionProgressUnit.getQuestionId()).thenReturn(123);
		
		
		questionFetcher = new QuestionFetcher(questionRepository);
	}
	
	@Test
	void successFetchQuestion() {
		when(questionRepository.findById(123)).thenReturn(Optional.of(question));
		
		Question fetchedQuestion = questionFetcher.fetchQuestion(questionProgress, 1);
		assertThat(fetchedQuestion).isEqualTo(question);
	}
	
	@Test
	void questionNotFound() {
		when(questionRepository.findById(123)).thenReturn(Optional.empty());
		
		AnswerChecker answerChecker = new AnswerChecker(questionRepository);
		assertThrows(UnknownQuestionException.class, () -> questionFetcher.fetchQuestion(questionProgress, 1));
	}
}
