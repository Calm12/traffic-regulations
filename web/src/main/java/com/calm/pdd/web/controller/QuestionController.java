package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class QuestionController {
	
	private SectionFetcher sectionFetcher;
	private QuestionFetcher questionFetcher;
	private AnswerChecker answerChecker;
	private ResultCollector resultCollector;
	
	public QuestionController(SectionFetcher sectionFetcher, QuestionFetcher questionFetcher, AnswerChecker answerChecker, ResultCollector resultCollector) {
		this.sectionFetcher = sectionFetcher;
		this.questionFetcher = questionFetcher;
		this.answerChecker = answerChecker;
		this.resultCollector = resultCollector;
	}
	
	@GetMapping("/section/{sectionId}")
	public RedirectView enterSection(@PathVariable int sectionId, HttpSession session) {
		QuestionProgress progress = sectionFetcher.fetchSection(sectionId);
		
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return new RedirectView(String.format("/section/%d/question/%d", sectionId, progress.getFirst().getQuestionNumber()));
	}
	
	@GetMapping("/section/{sectionId}/question/{questionNumber}")
	public ModelAndView question(ModelAndView model, @PathVariable int sectionId, @PathVariable int questionNumber, HttpSession session) {
		QuestionProgress progress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		if(progress == null) {
			return new ModelAndView("redirect:/sections");
		}
		
		if(!progress.isFixedSection(sectionId)) {
			return new ModelAndView("redirect:/sections");
		}
		
		Question question = questionFetcher.fetchQuestion(progress, questionNumber);
		
		model.addObject("question", question);
		model.addObject("progress", progress);
		model.setViewName("question");
		
		return model;
	}
	
	@PostMapping("/section/{sectionId}/question/{questionNumber}")
	public ModelAndView doAnswer(@PathVariable int sectionId, @PathVariable int questionNumber, @RequestParam int answer, HttpSession session) {
		final QuestionProgress progress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		String redirect;
		if(!answerChecker.checkAnswer(progress, questionNumber, answer)) {
			if(!progress.hasUnanswered()) {
				progress.setResult(resultCollector.collect(progress));
			}
			redirect = String.format("redirect:/section/%d/question/%d", sectionId, questionNumber);
		}
		else if(progress.hasNextUnanswered(questionNumber)) {
			redirect = String.format("redirect:/section/%d/question/%d", sectionId, progress.getNextUnanswered(questionNumber).getQuestionNumber());
		}
		else if(progress.hasUnanswered()) {
			redirect = String.format("redirect:/section/%d/question/%d", sectionId, progress.getFirstUnanswered().getQuestionNumber());
		}
		else {
			progress.setResult(resultCollector.collect(progress));
			redirect = String.format("redirect:/questions/%s/complete", progress.getId());
		}
		
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return new ModelAndView(redirect);
	}
}
