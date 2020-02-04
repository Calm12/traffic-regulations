package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.entity.UserStatistic;
import com.calm.pdd.core.model.repository.UserStatisticRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.Result;
import org.springframework.stereotype.Service;

@Service
public class ProgressCompleteHandler {
	private final ResultCollector resultCollector;
	private final UserStatisticRepository userStatisticRepository;
	
	public ProgressCompleteHandler(ResultCollector resultCollector, UserStatisticRepository userStatisticRepository) {
		this.resultCollector = resultCollector;
		this.userStatisticRepository = userStatisticRepository;
	}
	
	public Result handle(QuestionProgress progress, User user) {
		Result result = resultCollector.collect(progress);
		progress.setResult(result);
		
		UserStatistic statistic = userStatisticRepository
				.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Wtf?! User [" + user.getId() + "] has not UserStatistic!"));
		
		statistic.addToTotalTestingTime(result.getDuration());
		
		if(progress.isExam()) {
			statistic.incrementTotalExamAttempts();
			statistic.addToTotalExamScoreSum(result.getCorrect());
			
			if(!progress.hasTwoErrors()) {
				statistic.incrementSuccessExamAttempts();
				statistic.setTopExamTime(Math.min(statistic.getTopExamTime(), result.getDuration()));
			}
			
			if(progress.hasNoErrors()) {
				statistic.incrementExamAttemptsWithoutErrors();
			}
		}
		
		userStatisticRepository.save(statistic);
		
		return result;
	}
}
