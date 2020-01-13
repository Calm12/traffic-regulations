package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.repository.SectionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionFetcher {
	
	private QuestionRepository questionRepository;
	private SectionRepository sectionRepository;
	
	public SectionFetcher(QuestionRepository questionRepository, SectionRepository sectionRepository) {
		this.questionRepository = questionRepository;
		this.sectionRepository = sectionRepository;
	}
	
	public List<QuestionProgress> fetchSection(int sectionId) {
		List<Question> questions = questionRepository.getListBySectionId(sectionId);
		
		return questions.stream().map(q -> new QuestionProgress(q.getId(), q.getNumber())).collect(Collectors.toList());
	}
	
	public List<Section> fetchSectionsList() {
		return sectionRepository.findByOrderBySectionOrderAsc();
	}
}
