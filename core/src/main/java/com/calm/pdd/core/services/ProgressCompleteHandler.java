package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.Result;
import org.springframework.stereotype.Service;

@Service
public class ProgressCompleteHandler {
	private final ResultCollector resultCollector;
	
	public ProgressCompleteHandler(ResultCollector resultCollector) {
		this.resultCollector = resultCollector;
	}
	
	public Result handle(QuestionProgress progress, User user) {
		progress.setResult(resultCollector.collect(progress));
		
		return progress.getResult();
	}
}