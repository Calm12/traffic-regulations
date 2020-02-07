package com.calm.pdd.core.services.statistic;

import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.entity.UserStatistic;
import com.calm.pdd.core.model.repository.UserStatisticRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.Result;
import com.calm.pdd.core.services.ResultCollector;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProgressCompleteHandlerTest {
	
	@Mock
	private UserStatisticRepository userStatisticRepository;
	
	@Mock
	private ResultCollector resultCollector;
	
	@Mock
	private User user;
	
	@Mock
	private QuestionProgress questionProgress;
	
	@Captor
	private ArgumentCaptor<UserStatistic> captor;
	
	private Result result;
	
	private ProgressCompleteHandler progressCompleteHandler;
	
	@BeforeEach
	public void setUp() {
		result = new Result()
				.setTotal(20)
				.setCorrect(15)
				.setWrong(5)
				.setCorrectRate(75)
				.setWrongRate(25)
				.setDuration(35_000);
		
		when(resultCollector.collect(questionProgress)).thenReturn(result);
		when(user.getId()).thenReturn(123);
		when(userStatisticRepository.findByUserId(123)).thenReturn(Optional.of(new UserStatistic(123)));
		
		progressCompleteHandler = new ProgressCompleteHandler(resultCollector, userStatisticRepository);
	}
	
	@Test
	void successExamWithoutErrorAddToStatistic() {
		when(questionProgress.isExam()).thenReturn(true);
		when(questionProgress.hasTwoErrors()).thenReturn(false);
		when(questionProgress.hasNoErrors()).thenReturn(true);
		
		progressCompleteHandler.handle(questionProgress, user);
		
		verify(questionProgress).setResult(result);
		verify(userStatisticRepository).save(captor.capture());
		
		UserStatistic newStatistic = captor.getValue();
		
		assertThat(newStatistic.getUserId()).isEqualTo(123);
		assertThat(newStatistic.getTotalTestingTime()).isGreaterThanOrEqualTo(35_000);
		assertThat(newStatistic.getTotalExamAttempts()).isEqualTo(1);
		assertThat(newStatistic.getSuccessExamAttempts()).isEqualTo(1);
		assertThat(newStatistic.getExamAttemptsWithoutErrors()).isEqualTo(1);
		assertThat(newStatistic.getTotalExamScoreSum()).isEqualTo(15);
		assertThat(newStatistic.getTopExamTime()).isEqualTo(newStatistic.getTotalTestingTime());
	}
	
	@Test
	void successExamWithOneErrorAddToStatistic() {
		when(questionProgress.isExam()).thenReturn(true);
		when(questionProgress.hasTwoErrors()).thenReturn(false);
		when(questionProgress.hasNoErrors()).thenReturn(false);
		
		progressCompleteHandler.handle(questionProgress, user);
		
		verify(questionProgress).setResult(result);
		verify(userStatisticRepository).save(captor.capture());
		
		UserStatistic newStatistic = captor.getValue();
		
		assertThat(newStatistic.getUserId()).isEqualTo(123);
		assertThat(newStatistic.getTotalTestingTime()).isGreaterThanOrEqualTo(35_000);
		assertThat(newStatistic.getTotalExamAttempts()).isEqualTo(1);
		assertThat(newStatistic.getSuccessExamAttempts()).isEqualTo(1);
		assertThat(newStatistic.getExamAttemptsWithoutErrors()).isEqualTo(0);
		assertThat(newStatistic.getTotalExamScoreSum()).isEqualTo(15);
		assertThat(newStatistic.getTopExamTime()).isEqualTo(newStatistic.getTotalTestingTime());
	}
	
	@Test
	void failedExamWithMoreErrorsAddToStatistic() {
		when(questionProgress.isExam()).thenReturn(true);
		when(questionProgress.hasTwoErrors()).thenReturn(true);
		when(questionProgress.hasNoErrors()).thenReturn(false);
		
		progressCompleteHandler.handle(questionProgress, user);
		
		verify(questionProgress).setResult(result);
		verify(userStatisticRepository).save(captor.capture());
		
		UserStatistic newStatistic = captor.getValue();
		
		assertThat(newStatistic.getUserId()).isEqualTo(123);
		assertThat(newStatistic.getTotalTestingTime()).isGreaterThanOrEqualTo(35_000);
		assertThat(newStatistic.getTotalExamAttempts()).isEqualTo(1);
		assertThat(newStatistic.getSuccessExamAttempts()).isEqualTo(0);
		assertThat(newStatistic.getExamAttemptsWithoutErrors()).isEqualTo(0);
		assertThat(newStatistic.getTotalExamScoreSum()).isEqualTo(15);
		assertThat(newStatistic.getTopExamTime()).isEqualTo(Integer.MAX_VALUE);
	}
	
	@Test
	void completedNoExamAddToStatistic() {
		when(questionProgress.isExam()).thenReturn(false);
		
		progressCompleteHandler.handle(questionProgress, user);
		
		verify(questionProgress).setResult(result);
		verify(userStatisticRepository).save(captor.capture());
		
		UserStatistic newStatistic = captor.getValue();
		
		assertThat(newStatistic.getUserId()).isEqualTo(123);
		assertThat(newStatistic.getTotalTestingTime()).isGreaterThanOrEqualTo(35_000);
		assertThat(newStatistic.getTotalExamAttempts()).isEqualTo(0);
		assertThat(newStatistic.getSuccessExamAttempts()).isEqualTo(0);
		assertThat(newStatistic.getExamAttemptsWithoutErrors()).isEqualTo(0);
		assertThat(newStatistic.getTotalExamScoreSum()).isEqualTo(0);
		assertThat(newStatistic.getTopExamTime()).isEqualTo(Integer.MAX_VALUE);
	}
	
	@Test
	void userHasNotStatistic() {
		when(userStatisticRepository.findByUserId(123)).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, () -> progressCompleteHandler.handle(questionProgress, user));
	}
}
