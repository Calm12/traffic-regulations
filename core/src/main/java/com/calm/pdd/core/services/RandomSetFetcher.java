package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RandomSetFetcher {
	
	private static final int RANDOM_QUESTIONS_COUNT = 20;
	
	private QuestionRepository questionRepository;
	private QuestionProgressFactory questionProgressFactory;
	
	public RandomSetFetcher(QuestionRepository questionRepository, QuestionProgressFactory questionProgressFactory) {
		this.questionRepository = questionRepository;
		this.questionProgressFactory = questionProgressFactory;
	}
	
	public QuestionProgress fetchSet() {
		List<Question> questions = questionRepository.getRandomList(RANDOM_QUESTIONS_COUNT);
		
		return questionProgressFactory.buildProgressWithRandomQuestions(questions);
	}
}
