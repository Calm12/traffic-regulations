package com.calm.controller;

import com.calm.model.entity.Section;
import com.calm.model.repository.SectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SectionsController {
	
	private SectionRepository sectionRepository;
	
	public SectionsController(SectionRepository sectionRepository) {
		this.sectionRepository = sectionRepository;
	}
	
	@GetMapping("/sections")
	public ModelAndView list(ModelAndView model) {
		List<Section> sections = sectionRepository.findByOrderBySectionOrderAsc();
		model.addObject("sections", sections);
		model.setViewName("sections");
		
		return model;
	}
}
