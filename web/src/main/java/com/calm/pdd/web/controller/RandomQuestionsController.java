package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import com.calm.pdd.core.services.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
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
	public String question(Model model, @PathVariable int questionNumber, @SessionAttribute("QUESTIONS_PROGRESS") QuestionProgress progress) {
		if(!progress.isRandomSet()) {
			return "redirect:/sections";
		}
		
		Question question = questionFetcher.fetchQuestion(progress, questionNumber);
		
		model.addAttribute("question", question);
		model.addAttribute("currentProgressUnit", progress.getByNumber(questionNumber));
		model.addAttribute("progress", progress);
		
		return "question";
	}
	
	@PostMapping("/random/question/{questionNumber}")
	public String doAnswer(@PathVariable int questionNumber, @RequestParam int answer, HttpSession session, @AuthenticationPrincipal User user) {
		final QuestionProgress progress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		answerChecker.checkAnswer(progress, questionNumber, answer, user);
		Optional<QuestionProgressUnit> nextQuestion = progress.findNextQuestion(questionNumber);
		
		String redirect;
		if(nextQuestion.isPresent()) {
			redirect = String.format("redirect:/random/question/%d", nextQuestion.get().getQuestionNumber());
		}
		else {
			progress.setResult(resultCollector.collect(progress));
			redirect = String.format("redirect:/questions/%s/complete", progress.getId());
		}
		
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return redirect;
	}
}
