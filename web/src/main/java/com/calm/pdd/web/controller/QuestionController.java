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
		QuestionProgress questionProgress = sectionFetcher.fetchSection(sectionId);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		return new RedirectView(String.format("/section/%d/question/%d", sectionId, questionProgress.getFirst().getQuestionNumber()));
	}
	
	@GetMapping("/section/{sectionId}/question/{questionNumber}")
	public ModelAndView question(ModelAndView model, @PathVariable int sectionId, @PathVariable int questionNumber, HttpSession session) {
		QuestionProgress questionProgress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		if(questionProgress == null) {
			return new ModelAndView("redirect:/sections");
		}
		
		if(!questionProgress.isFixedSection(sectionId)) {
			return new ModelAndView("redirect:/sections");
		}
		
		Question question = questionFetcher.fetchQuestion(questionProgress, questionNumber);
		
		model.addObject("question", question);
		model.addObject("progress", questionProgress);
		model.setViewName("question");
		
		return model;
	}
	
	@PostMapping("/section/{sectionId}/question/{questionNumber}")
	public ModelAndView doAnswer(@PathVariable int sectionId, @PathVariable int questionNumber, @RequestParam int answer, HttpSession session) {
		QuestionProgress questionProgress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		QuestionProgress newQuestionProgress = answerChecker.checkAnswer(questionProgress, questionNumber, answer);
		
		session.setAttribute("QUESTIONS_PROGRESS", newQuestionProgress);
		
		if(newQuestionProgress.getByNumber(questionNumber).isWrongAnswered()) {
			//тут нужно проверить, есть ли еще неотвеченные
			return new ModelAndView(String.format("redirect:/section/%d/question/%d", sectionId, questionNumber));
		}
		else if(newQuestionProgress.hasNext(questionNumber)) {
			//тут нужно проверить следующий именно неотвеченный
			return new ModelAndView(String.format("redirect:/section/%d/question/%d", sectionId, questionNumber + 1));
		}
		else if(newQuestionProgress.hasUnanswered()) {
			return new ModelAndView(String.format("redirect:/section/%d/question/%d", sectionId, newQuestionProgress.getFirstUnanswered().getQuestionNumber()));
		}
		else {
			return new ModelAndView(String.format("redirect:/questions/%s/complete", newQuestionProgress.getId()));
		}
	}
	
	@GetMapping("/questions/{progressId}/complete")
	public ModelAndView complete(@PathVariable String progressId, HttpSession session) {
		QuestionProgress questionProgress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		if(questionProgress == null) {
			return new ModelAndView("redirect:/sections");
		}
		
		if(!questionProgress.getId().equals(progressId)) {
			return new ModelAndView("redirect:/sections");
		}
		
		if(questionProgress.getResult() == null) {
			questionProgress.setResult(resultCollector.collect(questionProgress));
			session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		}
		
		ModelAndView model = new ModelAndView();
		model.addObject("progress", questionProgress);
		model.setViewName("complete");
		
		return model;
	}
}
