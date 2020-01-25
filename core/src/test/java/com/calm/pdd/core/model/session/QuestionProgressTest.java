package com.calm.pdd.core.model.session;

import com.calm.pdd.core.exceptions.UnknownQuestionException;
import com.calm.pdd.core.model.enums.AnswerResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class QuestionProgressTest {
	
	private QuestionProgress progress;
	
	@BeforeEach
	public void setUp() {
		List<QuestionProgressUnit> progressUnits = Arrays.asList(
				new QuestionProgressUnit(51, 1).setAnswerResult(AnswerResult.WRONG),
				new QuestionProgressUnit(52, 2).setAnswerResult(AnswerResult.CORRECT),
				new QuestionProgressUnit(53, 3),
				new QuestionProgressUnit(54, 4),
				new QuestionProgressUnit(55, 5).setAnswerResult(AnswerResult.CORRECT),
				new QuestionProgressUnit(56, 6),
				new QuestionProgressUnit(57, 7),
				new QuestionProgressUnit(58, 8).setAnswerResult(AnswerResult.CORRECT),
				new QuestionProgressUnit(59, 9),
				new QuestionProgressUnit(60, 10).setAnswerResult(AnswerResult.WRONG)
		);
		
		progress = new QuestionProgress(progressUnits);
	}
	
	@Test
	void successGetFirstUnit() {
		QuestionProgressUnit firstUnit = progress.getFirst();
		assertThat(firstUnit.getQuestionId()).isEqualTo(51);
		assertThat(firstUnit.getQuestionNumber()).isEqualTo(1);
	}
	
	@Test
	void successGetByNumber() {
		QuestionProgressUnit firstUnit = progress.getByNumber(5);
		assertThat(firstUnit.getQuestionId()).isEqualTo(55);
		assertThat(firstUnit.getQuestionNumber()).isEqualTo(5);
	}
	
	@Test
	void tryToGetByWrongNumber() {
		assertThrows(UnknownQuestionException.class, () -> progress.getByNumber(13));
	}
	
	@Test
	void successFindNextQuestionSimple() {
		Optional<QuestionProgressUnit> nextQuestion = progress.findNextQuestion(4);
		assertThat(nextQuestion.isPresent()).isTrue();
		assertThat(nextQuestion.get().getQuestionNumber()).isEqualTo(6);
	}
	
	@Test
	void successFindNextQuestionReverse() {
		Optional<QuestionProgressUnit> nextQuestion = progress.findNextQuestion(9);
		assertThat(nextQuestion.isPresent()).isTrue();
		assertThat(nextQuestion.get().getQuestionNumber()).isEqualTo(3);
	}
	
	@Test
	void tryToFindNextQuestionWhenAllCompleted() {
		progress.getList().forEach(q -> q.setAnswerResult(AnswerResult.CORRECT));
		
		Optional<QuestionProgressUnit> nextQuestion = progress.findNextQuestion(9);
		assertThat(nextQuestion.isPresent()).isFalse();
	}
}
