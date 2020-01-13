package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.enums.AnswerResult;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AnswerChecker {
	
	private QuestionRepository questionRepository;
	
	public AnswerChecker(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}
	
	public List<QuestionProgress> checkAnswer(List<QuestionProgress> questionsProgress, int questionNumber, int answer) {
		QuestionProgress currentQuestionProgress = questionsProgress.get(questionNumber - 1);
		Question question = questionRepository.findById(currentQuestionProgress.getQuestionId()).orElseThrow(() -> new RuntimeException(String.format("Question %d not found!", currentQuestionProgress.getQuestionId())));
		
		if(question.getAnswer() == answer) {
			currentQuestionProgress.setAnswerResult(AnswerResult.CORRECT);
		}
		else {
			currentQuestionProgress.setAnswerResult(AnswerResult.WRONG);
		}
		currentQuestionProgress.setAnsweredNumber(answer);
		
		return questionsProgress;
	}
}
