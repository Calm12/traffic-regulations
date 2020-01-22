package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.services.SectionFetcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SectionsController {
	
	private SectionFetcher sectionFetcher;
	
	public SectionsController(SectionFetcher sectionFetcher) {
		this.sectionFetcher = sectionFetcher;
	}
	
	@GetMapping("/sections")
	public String list(Model model) {
		List<Section> sections = sectionFetcher.fetchSectionsList();
		model.addAttribute("sections", sections);
		
		return "sections";
	}
}
