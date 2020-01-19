package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.services.AnswerChecker;
import com.calm.pdd.core.services.QuestionFetcher;
import com.calm.pdd.core.services.SectionFetcher;
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
	
	public QuestionController(SectionFetcher sectionFetcher, QuestionFetcher questionFetcher, AnswerChecker answerChecker) {
		this.sectionFetcher = sectionFetcher;
		this.questionFetcher = questionFetcher;
		this.answerChecker = answerChecker;
	}
	
	@GetMapping("/section/{sectionId}")
	public RedirectView enterSection(@PathVariable int sectionId, HttpSession session) {
		QuestionProgress questionProgress = sectionFetcher.fetchSection(sectionId);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		session.setAttribute("CURRENT_SECTION", sectionId); //это можно ваще хранить в объекте прогресса
		
		return new RedirectView(String.format("/section/%d/question/%d", sectionId, questionProgress.getFirst().getQuestionNumber()));
	}
	
	@GetMapping("/section/{sectionId}/question/{questionNumber}")
	public ModelAndView question(ModelAndView model, @PathVariable int sectionId, @PathVariable int questionNumber, HttpSession session) {
		if((int)session.getAttribute("CURRENT_SECTION") != sectionId) {
			log.warn("Wrong section!");
			return new ModelAndView("redirect:/sections");
		}
		
		QuestionProgress questionProgress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
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
			return new ModelAndView(String.format("redirect:/section/%d/question/%d", sectionId, questionNumber));
		}
		else {
			//если следующий вопрос существует, то редирект, иначе надо вернуть туда же, и можно показать алерт с поздравлением (а точнее, если еще существуют неотвеченные вопросы)
			//Вариант искать первый неотвеченный и кидать на него, а если все отвечены, то на страницу с поздравлением. Это все только если следующий вопрос не существует.
			return new ModelAndView(String.format("redirect:/section/%d/question/%d", sectionId, questionNumber + 1));
		}
	}
}
