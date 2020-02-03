package com.calm.pdd.core.model.session;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.Section;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionProgressFactory {
	
	public QuestionProgress buildProgressWithFixedSection(List<Question> questions, int sectionId) {
		Section section = questions.get(0).getSection();
		
		QuestionProgress progress = new QuestionProgress(toProgressList(questions));
		progress.setSectionId(sectionId);
		progress.setProgressName(String.format("%s. %s", section.getNumber(), section.getName()));
		progress.setSectionType(SectionType.FIXED);
		
		return progress;
	}
	
	public QuestionProgress buildProgressWithRandomQuestions(List<Question> questions) {
		QuestionProgress progress = new QuestionProgress(toProgressListWithNumbersOverride(questions));
		progress.setSectionId(0);
		progress.setProgressName("20 случайных вопросов");
		progress.setSectionType(SectionType.RANDOM);
		
		return progress;
	}
	
	public QuestionProgress buildProgressForExam(List<Question> questions) {
		QuestionProgress progress = new QuestionProgress(toProgressListWithNumbersOverride(questions));
		progress.setSectionId(0);
		progress.setProgressName("Экзамен");
		progress.setSectionType(SectionType.EXAM);
		
		return progress;
	}
	
	private List<QuestionProgressUnit> toProgressList(List<Question> questions) {
		return questions.stream().map(q -> new QuestionProgressUnit(q.getId(), q.getNumber())).collect(Collectors.toList());
	}
	
	private List<QuestionProgressUnit> toProgressListWithNumbersOverride(List<Question> questions) {
		List<QuestionProgressUnit> list = new ArrayList<>();
		
		int number = 1;
		for(Question q : questions) {
			QuestionProgressUnit questionProgressUnit = new QuestionProgressUnit(q.getId(), number);
			list.add(questionProgressUnit);
			number++;
		}
		
		return list;
	}
}
