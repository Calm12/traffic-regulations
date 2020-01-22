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
	
	/**
	 * Mutates the passed QuestionProgress object.
	 * @return boolean correct/wrong answer
	 */
	public boolean checkAnswer(QuestionProgress progress, int questionNumber, int answer) {
		QuestionProgressUnit progressUnit = progress.getByNumber(questionNumber);
		Question question = questionRepository.findById(progressUnit.getQuestionId()).orElseThrow(() -> new RuntimeException(String.format("Question %d not found!", progressUnit.getQuestionId())));
		
		if(question.getAnswer() == answer) {
			progressUnit.setAnswerResult(AnswerResult.CORRECT);
		}
		else {
			progressUnit.setAnswerResult(AnswerResult.WRONG);
		}
		progressUnit.setAnsweredNumber(answer);
		
		return progressUnit.isCorrectAnswered();
	}
}
