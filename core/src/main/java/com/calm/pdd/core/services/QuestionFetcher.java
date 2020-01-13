package com.calm.pdd.core.services;

import com.calm.pdd.core.exceptions.UnknownQuestionException;
import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionFetcher {
	
	private QuestionRepository questionRepository;
	
	public QuestionFetcher(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}
	
	public Question fetchQuestion(List<QuestionProgress> questionsProgress, int questionNumber) {
		try {
			QuestionProgress currentQuestionProgress = questionsProgress.get(questionNumber - 1);
			
			return questionRepository.findById(currentQuestionProgress.getQuestionId()).orElseThrow(() -> new UnknownQuestionException(String.format("Question %d not found!", currentQuestionProgress.getQuestionId())));
		}
		catch(IndexOutOfBoundsException e) {
			throw new UnknownQuestionException("Wrong question number for this section!");
		}
	}
}
