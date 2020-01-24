package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.enums.AnswerResult;
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
class AnswerCheckerTest {
	
	@Mock
	private QuestionRepository questionRepository;
	
	@Mock
	private QuestionProgress questionProgress;
	
	@Mock
	private QuestionProgressUnit questionProgressUnit;
	
	@Mock
	private Question question;
	
	private AnswerChecker answerChecker;
	
	@BeforeEach
	public void setUp() {
		when(questionProgress.getByNumber(1)).thenReturn(questionProgressUnit);
		when(questionProgressUnit.getQuestionId()).thenReturn(123);
		when(questionRepository.findById(123)).thenReturn(Optional.of(question));
		lenient().when(question.getAnswer()).thenReturn(15);
		
		answerChecker = new AnswerChecker(questionRepository);
	}
	
	@Test
	void checkCorrectAnswer() {
		answerChecker.checkAnswer(questionProgress, 1, 15);
		
		verify(questionProgressUnit).setAnswerResult(AnswerResult.CORRECT);
		verify(questionProgressUnit).setAnsweredNumber(15);
	}
	
	@Test
	void checkWrongAnswer() {
		answerChecker.checkAnswer(questionProgress, 1, 20);
		
		verify(questionProgressUnit).setAnswerResult(AnswerResult.WRONG);
		verify(questionProgressUnit).setAnsweredNumber(20);
	}
	
	@Test
	void questionNotFoundWtf() {
		when(questionRepository.findById(123)).thenReturn(Optional.empty());
		
		AnswerChecker answerChecker = new AnswerChecker(questionRepository);
		assertThrows(RuntimeException.class, () -> answerChecker.checkAnswer(questionProgress, 1, 15));
	}
}
