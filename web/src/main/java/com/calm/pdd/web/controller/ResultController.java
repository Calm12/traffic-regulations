package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.session.QuestionProgress;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class ResultController {
	
	@GetMapping("/questions/{progressId}/complete")
	public String complete(Model model, @PathVariable String progressId, @SessionAttribute("QUESTIONS_PROGRESS") QuestionProgress progress) {
		if(progress == null) {
			return "redirect:/sections";
		}
		
		if(!progress.getId().equals(progressId)) {
			return "redirect:/sections";
		}
		
		model.addAttribute("progress", progress);
		
		return "complete";
	}
}
