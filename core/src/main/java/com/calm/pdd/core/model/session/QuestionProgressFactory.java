package com.calm.pdd.core.model.session;

import com.calm.pdd.core.model.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionProgressFactory {
	
	public QuestionProgress buildProgressWithFixedSection(List<Question> questions, int sectionId) {
		QuestionProgress questionProgress = new QuestionProgress(buildQuestionProgressList(questions));
		questionProgress.setSectionId(sectionId);
		questionProgress.setSectionType(SectionType.FIXED);
		
		return questionProgress;
	}
	
	public QuestionProgress buildProgressWithRandomQuestions(List<Question> questions) {
		QuestionProgress questionProgress = new QuestionProgress(buildQuestionProgressList(questions));
		questionProgress.setSectionId(0);
		questionProgress.setSectionType(SectionType.RANDOM);
		
		return questionProgress;
	}
	
	private List<QuestionProgressUnit> buildQuestionProgressList(List<Question> questions) {
		return questions.stream().map(q -> new QuestionProgressUnit(q.getId(), q.getNumber())).collect(Collectors.toList());
	}
}
