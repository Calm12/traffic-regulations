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
public class RandomQuestionsController {
	
	private RandomSetFetcher randomSetFetcher;
	private QuestionFetcher questionFetcher;
	private AnswerChecker answerChecker;
	private ResultCollector resultCollector;
	
	public RandomQuestionsController(RandomSetFetcher randomSetFetcher, QuestionFetcher questionFetcher, AnswerChecker answerChecker, ResultCollector resultCollector) {
		this.randomSetFetcher = randomSetFetcher;
		this.questionFetcher = questionFetcher;
		this.answerChecker = answerChecker;
		this.resultCollector = resultCollector;
	}
	
	@GetMapping("/random")
	public RedirectView enterSection(HttpSession session) {
		QuestionProgress progress = randomSetFetcher.fetchSet();
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return new RedirectView(String.format("/random/question/%d", progress.getFirst().getQuestionNumber()));
	}
	
	@GetMapping("/random/question/{questionNumber}")
	public ModelAndView question(ModelAndView model, @PathVariable int questionNumber, HttpSession session) {
		QuestionProgress progress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		if(progress == null) {
			return new ModelAndView("redirect:/sections");
		}
		
		if(!progress.isRandomSet()) {
			return new ModelAndView("redirect:/sections");
		}
		
		Question question = questionFetcher.fetchQuestion(progress, questionNumber);
		
		model.addObject("question", question);
		model.addObject("currentProgressUnit", progress.getByNumber(questionNumber));
		model.addObject("progress", progress);
		model.setViewName("question");
		
		return model;
	}
	
	@PostMapping("/random/question/{questionNumber}")
	public ModelAndView doAnswer(@PathVariable int questionNumber, @RequestParam int answer, HttpSession session) {
		final QuestionProgress progress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		answerChecker.checkAnswer(progress, questionNumber, answer);
		
		String redirect;
		if(progress.hasNextUnanswered(questionNumber)) {
			redirect = String.format("redirect:/random/question/%d", progress.getNextUnanswered(questionNumber).getQuestionNumber());
		}
		else if(progress.hasUnanswered()) {
			redirect = String.format("redirect:/random/question/%d", progress.getFirstUnanswered().getQuestionNumber());
		}
		else {
			progress.setResult(resultCollector.collect(progress));
			redirect = String.format("redirect:/questions/%s/complete", progress.getId());
		}
		
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return new ModelAndView(redirect);
	}
}
