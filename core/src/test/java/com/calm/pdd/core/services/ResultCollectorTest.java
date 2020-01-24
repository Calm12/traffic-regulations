package com.calm.pdd.core.services;

import com.calm.pdd.core.model.enums.AnswerResult;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import com.calm.pdd.core.model.session.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ResultCollectorTest {
	
	@Mock
	private QuestionProgress questionProgress;
	
	@BeforeEach
	public void setUp() {
		List<QuestionProgressUnit> progressList = Arrays.asList(
				new QuestionProgressUnit(51, 1).setAnswerResult(AnswerResult.WRONG),
				new QuestionProgressUnit(52, 2).setAnswerResult(AnswerResult.WRONG),
				new QuestionProgressUnit(53, 3).setAnswerResult(AnswerResult.CORRECT),
				new QuestionProgressUnit(54, 4).setAnswerResult(AnswerResult.CORRECT),
				new QuestionProgressUnit(55, 5).setAnswerResult(AnswerResult.CORRECT)
		);
		
		when(questionProgress.getList()).thenReturn(progressList);
		when(questionProgress.getStartTime()).thenReturn(System.currentTimeMillis() - 25_000);
	}
	
	@Test
	void successCollectResult() {
		ResultCollector resultCollector = new ResultCollector();
		Result result = resultCollector.collect(questionProgress);
		
		assertThat(result.getTotal()).isEqualTo(5);
		assertThat(result.getWrong()).isEqualTo(2);
		assertThat(result.getCorrect()).isEqualTo(3);
		assertThat(result.getCorrectRate()).isEqualTo(60);
		assertThat(result.getWrongRate()).isEqualTo(40);
		assertThat(result.getDuration()).isGreaterThan(25_000L);
	}
	
}
