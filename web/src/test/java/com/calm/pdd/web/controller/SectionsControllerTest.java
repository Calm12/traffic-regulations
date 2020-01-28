package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.services.SectionFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SectionsControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private SectionFetcher sectionFetcher;
	
	@Mock
	private Section section;
	
	private List<Section> sections;
	
	@BeforeEach
	public void setUp() {
		sections = Collections.singletonList(section);
		when(sectionFetcher.fetchSectionsList()).thenReturn(sections);
		
		mockMvc = MockMvcBuilders.standaloneSetup(new SectionsController(sectionFetcher))
				.build();
	}
	
	@Test
	void successListAction() throws Exception {
		mockMvc.perform(get("/sections/"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("sections"))
				.andExpect(model().attribute("sections", sections))
				.andExpect(view().name("sections"))
				.andReturn();
	}
}
