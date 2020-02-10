package com.calm.pdd.core.services.statistic;

import com.calm.pdd.core.exceptions.UserHasNotStatisticException;
import com.calm.pdd.core.model.entity.Coverage;
import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.entity.UserStatistic;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.repository.SectionRepository;
import com.calm.pdd.core.model.repository.UserStatisticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticFetcherTest {
	
	@Mock
	private UserStatisticRepository userStatisticRepository;
	
	@Mock
	private QuestionRepository questionRepository;
	
	@Mock
	private SectionRepository sectionRepository;
	
	@Mock
	private User user;
	
	@Mock
	private Section section;
	
	private StatisticFetcher statisticFetcher;
	
	@BeforeEach
	public void setUp() {
		when(user.getId()).thenReturn(123);
		
		when(questionRepository.count()).thenReturn(150L);
		when(questionRepository.countBySectionId(anyInt())).thenReturn(20);
		
		when(section.getId()).thenReturn(1);
		when(section.getNumber()).thenReturn("1");
		when(section.getName()).thenReturn("section_1");
		when(sectionRepository.findByOrderBySectionOrderAsc()).thenReturn(Collections.singletonList(section));
		
		Set<Coverage> coverages = new HashSet<>();
		coverages.add(new Coverage(1, 1, true));
		coverages.add(new Coverage(2, 1, false));
		coverages.add(new Coverage(3, 1, false));
		coverages.add(new Coverage(4, 1, true));
		coverages.add(new Coverage(5, 1, true));
		coverages.add(new Coverage(6, 1, false));
		coverages.add(new Coverage(7, 1, true));
		coverages.add(new Coverage(8, 1, true));
		coverages.add(new Coverage(9, 1, true));
		coverages.add(new Coverage(10, 1, false));
		
		UserStatistic userStatistic = new UserStatistic(123)
				.setTotalExamAttempts(10)
				.setSuccessExamAttempts(5)
				.setExamAttemptsWithoutErrors(1)
				.setTopExamTime(15_000)
				.setTotalTestingTime(1_815_000)
				.setTotalExamScoreSum(150)
				.setCoverages(coverages);
		
		when(userStatisticRepository.findByUserId(123)).thenReturn(Optional.of(userStatistic));
		
		statisticFetcher = new StatisticFetcher(userStatisticRepository, questionRepository, sectionRepository);
	}
	
	@Test
	void test() {
		Statistic statistic = statisticFetcher.fetch(user);
		
		assertThat(statistic.getTotalTestingTime()).isEqualTo("00:30:15");
		assertThat(statistic.getTopExamTime()).isEqualTo("00:00:15");
		assertThat(statistic.getTotalExamAttempts()).isEqualTo(10);
		assertThat(statistic.getSuccessExamAttempts()).isEqualTo(5);
		assertThat(statistic.getExamAttemptsWithoutErrors()).isEqualTo(1);
		assertThat(statistic.getAverageExamScore()).isEqualTo(15);
		assertThat(statistic.getCompletedPartRate()).isEqualTo(6);
		assertThat(statistic.getCompletedPartSuccessRate()).isEqualTo(60);
		
		assertThat(statistic.getCoverage().size()).isEqualTo(1);
		SectionCoverage sectionCoverage = statistic.getCoverage().iterator().next();
		assertThat(sectionCoverage.getSectionId()).isEqualTo(1);
		assertThat(sectionCoverage.getSectionName()).isEqualTo("1. section_1");
		assertThat(sectionCoverage.getTotalQuestions()).isEqualTo(20);
		assertThat(sectionCoverage.getCorrectAnswered()).isEqualTo(6);
		assertThat(sectionCoverage.getWrongAnswered()).isEqualTo(4);
		assertThat(sectionCoverage.getTotalAnswered()).isEqualTo(10);
		
	}
	
	@Test
	void userHasNoStatistic() {
		when(userStatisticRepository.findByUserId(123)).thenReturn(Optional.empty());
		assertThrows(UserHasNotStatisticException.class, () -> statisticFetcher.fetch(user));
	}
}