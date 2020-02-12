package com.calm.pdd.core.services.statistic;

import com.calm.pdd.core.exceptions.UserHasNotStatisticException;
import com.calm.pdd.core.model.entity.Coverage;
import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.entity.UserStatistic;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.repository.SectionRepository;
import com.calm.pdd.core.model.repository.UserStatisticRepository;
import com.calm.pdd.core.util.Duration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatisticFetcher {
	
	private final UserStatisticRepository userStatisticRepository;
	private final QuestionRepository questionRepository;
	private final SectionRepository sectionRepository;
	
	public StatisticFetcher(UserStatisticRepository userStatisticRepository, QuestionRepository questionRepository, SectionRepository sectionRepository) {
		this.userStatisticRepository = userStatisticRepository;
		this.questionRepository = questionRepository;
		this.sectionRepository = sectionRepository;
	}
	
	public Statistic fetch(User user) {
		UserStatistic userStatistic = userStatisticRepository.findByUserId(user.getId()).orElseThrow(() -> new UserHasNotStatisticException(user));
		Set<Coverage> rawCoverages = userStatistic.getCoverages();
		
		long totalQuestionsCount = questionRepository.count();
		long correctAnswersCount = rawCoverages.stream().filter(c -> c.isCorrect()).count();
		
		int completedPartRate = (int) (((float) rawCoverages.size() / (float) totalQuestionsCount) * 100);
		int completedPartSuccessRate = (int) (((float) correctAnswersCount / (float) rawCoverages.size()) * 100);
		
		List<Section> sections = sectionRepository.findByOrderBySectionOrderAsc();
		
		List<SectionCoverage> coverage = new ArrayList<>();
		for(Section section : sections) {
			List<Coverage> rawSectionCoverage = rawCoverages.stream().filter(c -> c.getSectionId() == section.getId()).collect(Collectors.toList());
			int correctAnswered = (int) rawSectionCoverage.stream().filter(c -> c.isCorrect()).count();
			
			int totalQuestions = questionRepository.countBySectionId(section.getId());
			int correctAnsweredPart = (int) (((float) correctAnswered / (float) totalQuestions) * 100);
			
			SectionCoverage sectionCoverage = SectionCoverage.builder()
					.sectionId(section.getId())
					.sectionName(String.format("%s. %s", section.getNumber(), section.getName()))
					.totalQuestions(totalQuestions)
					.correctAnswered(correctAnswered)
					.wrongAnswered(rawSectionCoverage.size() - correctAnswered)
					.correctAnsweredPart(correctAnsweredPart)
					.build();
			
			coverage.add(sectionCoverage);
		}
		
		return Statistic.builder()
				.totalTestingTime(Duration.humanize(userStatistic.getTotalTestingTime()))
				.topExamTime(userStatistic.getTopExamTime() != Integer.MAX_VALUE ? Duration.humanize(userStatistic.getTopExamTime()) : "—")
				.totalExamAttempts(userStatistic.getTotalExamAttempts())
				.successExamAttempts(userStatistic.getSuccessExamAttempts())
				.examAttemptsWithoutErrors(userStatistic.getExamAttemptsWithoutErrors())
				.averageExamScore(userStatistic.getAverageExamScore())
				.completedPartRate(completedPartRate)
				.completedPartSuccessRate(completedPartSuccessRate)
				.coverage(Collections.unmodifiableList(coverage))
				.build();
	}
}
