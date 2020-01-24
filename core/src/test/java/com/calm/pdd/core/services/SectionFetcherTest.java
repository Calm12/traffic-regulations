package com.calm.pdd.core.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.repository.SectionRepository;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class SectionFetcherTest {
	
	@Mock
	private QuestionRepository questionRepository;
	
	@Mock
	private SectionRepository sectionRepository;
	
	@Mock
	private QuestionProgressFactory questionProgressFactory;
	
	@Mock
	private QuestionProgress questionProgress;
	
	@Mock
	private Question question;
	
	@Mock
	private Section section;
	
	private SectionFetcher sectionFetcher;
	
	private List<Section> sectionsList;
	
	@BeforeEach
	public void setUp() {
		sectionFetcher = new SectionFetcher(questionRepository, sectionRepository, questionProgressFactory);
	}
	
	@Test
	void successFetchSection() {
		List<Question> questionsList = Collections.singletonList(question);
		when(questionRepository.getListBySectionId(1)).thenReturn(questionsList);
		when(questionProgressFactory.buildProgressWithFixedSection(questionsList, 1)).thenReturn(questionProgress);
		
		QuestionProgress fetchedQuestionProgress = sectionFetcher.fetchSection(1);
		assertThat(fetchedQuestionProgress).isEqualTo(questionProgress);
	}
	
	@Test
	void successFetchSectionsList() {
		sectionsList = Collections.singletonList(section);
		when(sectionRepository.findByOrderBySectionOrderAsc()).thenReturn(sectionsList);
		
		List<Section> sections = sectionFetcher.fetchSectionsList();
		assertThat(sections).isEqualTo(sectionsList);
	}
}
