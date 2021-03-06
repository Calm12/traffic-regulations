package com.calm.pdd.core.services;

import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.Result;
import com.calm.pdd.core.util.Duration;
import org.springframework.stereotype.Service;

@Service
public class ResultCollector {
	
	public Result collect(QuestionProgress progress) {
		Result result = new Result();
		
		result.setTotal(progress.getList().size());
		result.setCorrect((int) progress.getList().stream().filter(q -> q.isCorrectAnswered()).count());
		result.setWrong((int) progress.getList().stream().filter(q -> q.isWrongAnswered()).count());
		
		result.setCorrectRate(Math.round((float) result.getCorrect() / result.getTotal() * 100));
		result.setWrongRate(100 - result.getCorrectRate());
		
		result.setDuration((int)(System.currentTimeMillis() - progress.getStartTime()));
		result.setFormattedDuration(Duration.humanize(result.getDuration()));
		
		return result;
	}
}
