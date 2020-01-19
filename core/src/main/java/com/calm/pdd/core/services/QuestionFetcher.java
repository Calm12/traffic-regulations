package com.calm.pdd.core.services;

import com.calm.pdd.core.exceptions.UnknownQuestionException;
import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuestionFetcher {
	
	private QuestionRepository questionRepository;
	
	public QuestionFetcher(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}
	
	public Question fetchQuestion(QuestionProgress questionProgress, int questionNumber) {
		QuestionProgressUnit currentQuestionProgressUnit = questionProgress.getByNumber(questionNumber);
		
		return questionRepository.findById(currentQuestionProgressUnit.getQuestionId()).orElseThrow(() -> new UnknownQuestionException(String.format("Question %d not found!", currentQuestionProgressUnit.getQuestionId())));
	}
}
