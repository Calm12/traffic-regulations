package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.session.QuestionProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class ResultController {
	
	@GetMapping("/questions/{progressId}/complete")
	public ModelAndView complete(@PathVariable String progressId, @SessionAttribute("QUESTIONS_PROGRESS") QuestionProgress progress) {
		if(progress == null) {
			return new ModelAndView("redirect:/sections");
		}
		
		if(!progress.getId().equals(progressId)) {
			return new ModelAndView("redirect:/sections");
		}
		
		ModelAndView model = new ModelAndView();
		model.addObject("progress", progress);
		model.setViewName("complete");
		
		return model;
	}
}
