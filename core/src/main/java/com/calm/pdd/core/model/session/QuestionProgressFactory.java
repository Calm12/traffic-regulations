package com.calm.pdd.core.model.session;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.Section;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionProgressFactory {
	
	public QuestionProgress buildProgressWithFixedSection(List<Question> questions, int sectionId) {
		Section section = questions.get(0).getSection();
		
		QuestionProgress questionProgress = new QuestionProgress(buildQuestionProgressList(questions));
		questionProgress.setSectionId(sectionId);
		questionProgress.setProgressName(String.format("%s. %s", section.getNumber(), section.getName()));
		questionProgress.setSectionType(SectionType.FIXED);
		
		return questionProgress;
	}
	
	public QuestionProgress buildProgressWithRandomQuestions(List<Question> questions) {
		QuestionProgress questionProgress = new QuestionProgress(buildQuestionProgressList(questions));
		questionProgress.setSectionId(0);
		questionProgress.setProgressName("20 случайных вопросов");
		questionProgress.setSectionType(SectionType.RANDOM);
		
		return questionProgress;
	}
	
	private List<QuestionProgressUnit> buildQuestionProgressList(List<Question> questions) {
		return questions.stream().map(q -> new QuestionProgressUnit(q.getId(), q.getNumber())).collect(Collectors.toList());
	}
}
