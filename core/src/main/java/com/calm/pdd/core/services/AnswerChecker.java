package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.enums.AnswerResult;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnswerChecker {
	
	private QuestionRepository questionRepository;
	
	public AnswerChecker(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}
	
	public QuestionProgress checkAnswer(QuestionProgress questionProgress, int questionNumber, int answer) {
		QuestionProgressUnit currentQuestionProgressUnit = questionProgress.getByNumber(questionNumber);
		Question question = questionRepository.findById(currentQuestionProgressUnit.getQuestionId()).orElseThrow(() -> new RuntimeException(String.format("Question %d not found!", currentQuestionProgressUnit.getQuestionId())));
		
		if(question.getAnswer() == answer) {
			currentQuestionProgressUnit.setAnswerResult(AnswerResult.CORRECT);
		}
		else {
			currentQuestionProgressUnit.setAnswerResult(AnswerResult.WRONG);
		}
		currentQuestionProgressUnit.setAnsweredNumber(answer);
		
		return questionProgress;
	}
}
