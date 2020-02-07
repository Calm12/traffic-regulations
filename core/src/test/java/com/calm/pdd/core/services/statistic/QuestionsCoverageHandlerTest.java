package com.calm.pdd.core.services.statistic;

import com.calm.pdd.core.IntegrationTestApplication;
import com.calm.pdd.core.config.spring.PersistenceContext;
import com.calm.pdd.core.model.entity.*;
import com.calm.pdd.core.model.repository.UserStatisticRepository;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = {PersistenceContext.class, IntegrationTestApplication.class})
class QuestionsCoverageHandlerTest {
	
	@Autowired
	private UserStatisticRepository userStatisticRepository;
	
	@Mock
	private User user;
	
	@Mock
	private QuestionProgressUnit questionProgressUnit;
	
	@Mock
	private Question question;
	
	@Mock
	private Section section;
	
	@Autowired
	private QuestionsCoverageHandler questionsCoverageHandler;
	
	@BeforeEach
	public void setUp() {
		when(user.getId()).thenReturn(123);
		lenient().when(questionProgressUnit.isCorrectAnswered()).thenReturn(true);
		lenient().when(question.getSection()).thenReturn(section);
		lenient().when(section.getId()).thenReturn(20);
		
		UserStatistic statistic = new UserStatistic(123);
		statistic.addCoverage(new Coverage(1, 20, false));
		
		userStatisticRepository.save(statistic);
	}
	
	@Test
	@Transactional
	void updateAlreadyAnsweredQuestion() {
		when(question.getId()).thenReturn(1);
		questionsCoverageHandler.updateCoverage(user, question, questionProgressUnit);
		
		Set<Coverage> coverages = userStatisticRepository.findByUserId(123).orElseThrow(() -> new NullPointerException()).getCoverages();
		assertThat(coverages.size()).isEqualTo(1);
		
		Coverage coverage = coverages.iterator().next();
		assertThat(coverage.getQuestionId()).isEqualTo(1);
		assertThat(coverage.isCorrect()).isTrue();
	}
	
	@Test
	@Transactional
	void answerNewQuestion() {
		when(question.getId()).thenReturn(2);
		questionsCoverageHandler.updateCoverage(user, question, questionProgressUnit);
		
		Set<Coverage> coverages = userStatisticRepository.findByUserId(123).orElseThrow(() -> new NullPointerException()).getCoverages();
		assertThat(coverages.size()).isEqualTo(2);
		assertThat(coverages.stream().anyMatch(c -> c.isCorrect() && c.getQuestionId() == 2)).isTrue();
	}

	@Test
	void userHasNotStatistic() {
		when(user.getId()).thenReturn(456);
		assertThrows(RuntimeException.class, () -> questionsCoverageHandler.updateCoverage(user, question, questionProgressUnit));
	}
}
