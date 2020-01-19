package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.repository.SectionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionFetcher {
	
	private QuestionRepository questionRepository;
	private SectionRepository sectionRepository;
	private QuestionProgressFactory questionProgressFactory;
	
	public SectionFetcher(QuestionRepository questionRepository, SectionRepository sectionRepository, QuestionProgressFactory questionProgressFactory) {
		this.questionRepository = questionRepository;
		this.sectionRepository = sectionRepository;
		this.questionProgressFactory = questionProgressFactory;
	}
	
	public QuestionProgress fetchSection(int sectionId) {
		List<Question> questions = questionRepository.getListBySectionId(sectionId);
		
		return questionProgressFactory.buildProgressWithFixedSection(questions, sectionId);
	}
	
	public List<Section> fetchSectionsList() {
		return sectionRepository.findByOrderBySectionOrderAsc();
	}
}
