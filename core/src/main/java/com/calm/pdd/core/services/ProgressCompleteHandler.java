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
		
		/*
		 * Возможно, позже добавлю режим "экзамен", а пока экзаменом считаем 20 рандомных вопросов с 2 и менее ошибками.
		 * Если ошибок больше, считаем это неудачной попыткой экзамена
		 */
		if(progress.isRandomSet()) { //progress.isExam()
			if(hasLessThanTwoErrors(progress)) {
				statistic.setTopExamTime(Math.min(statistic.getTopExamTime(), result.getDuration()));
				statistic.incrementSuccessExamAttempts();
				
				if(hasNoErrors(progress)) {
					statistic.incrementExamAttemptsWithoutErrors();
				}
			}
			else {
				statistic.incrementTotalExamAttempts();
			}
		}
		
		
		
		//TODO save statistic
		
		return result;
	}
	
	private boolean hasNoErrors(QuestionProgress progress) {
		return hasErrorsLessThan(progress, 2);
	}
	
	private boolean hasLessThanTwoErrors(QuestionProgress progress) {
		return hasErrorsLessThan(progress, 2);
	}
	
	private boolean hasErrorsLessThan(QuestionProgress progress, int lessThan) {
		return progress.getList().stream().filter(q -> q.isWrongAnswered()).count() < lessThan;
	}
}
