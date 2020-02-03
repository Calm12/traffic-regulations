package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamSetFetcher {
	
	private static final int EXAM_QUESTIONS_COUNT = 20;
	
	private QuestionRepository questionRepository;
	private QuestionProgressFactory questionProgressFactory;
	
	public ExamSetFetcher(QuestionRepository questionRepository, QuestionProgressFactory questionProgressFactory) {
		this.questionRepository = questionRepository;
		this.questionProgressFactory = questionProgressFactory;
	}
	
	public QuestionProgress fetchSet() {
		List<Question> questions = questionRepository.getRandomList(EXAM_QUESTIONS_COUNT);
		
		return questionProgressFactory.buildProgressForExam(questions);
	}
}
