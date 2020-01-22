package com.calm.pdd.core.services;

import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import com.calm.pdd.core.model.session.Result;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Service;

@Service
public class ResultCollector {
	
	public Result collect(QuestionProgress progress) {
		Result result = new Result();
		
		result.setTotal(progress.getList().size());
		result.setCorrect((int) progress.getList().stream().filter(q -> !q.isWrongAnswered()).count());
		result.setWrong((int) progress.getList().stream().filter(QuestionProgressUnit::isWrongAnswered).count());
		
		result.setCorrectRate(Math.round((float) result.getCorrect() / result.getTotal() * 100));
		result.setWrongRate(100 - result.getCorrectRate());
		
		result.setDuration(System.currentTimeMillis() - progress.getStartTime());
		result.setFormattedDuration(humanizeDuration(result.getDuration()));
		
		return result;
	}
	
	private String humanizeDuration(long time) {
		return DurationFormatUtils.formatDuration(time, "HH:mm:ss");
	}
}
