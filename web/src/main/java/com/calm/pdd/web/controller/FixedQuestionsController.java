package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import com.calm.pdd.core.services.*;
import com.calm.pdd.core.services.statistic.ProgressCompleteHandler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class FixedQuestionsController {
	
	private SectionFetcher sectionFetcher;
	private QuestionFetcher questionFetcher;
	private AnswerChecker answerChecker;
	private ProgressCompleteHandler progressCompleteHandler;
	
	public FixedQuestionsController(SectionFetcher sectionFetcher, QuestionFetcher questionFetcher, AnswerChecker answerChecker, ProgressCompleteHandler progressCompleteHandler) {
		this.sectionFetcher = sectionFetcher;
		this.questionFetcher = questionFetcher;
		this.answerChecker = answerChecker;
		this.progressCompleteHandler = progressCompleteHandler;
	}
	
	@GetMapping("/section/{sectionId}")
	public RedirectView enterSection(@PathVariable int sectionId, HttpSession session) {
		QuestionProgress progress = sectionFetcher.fetchSection(sectionId);
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return new RedirectView(String.format("/section/%d/question/%d", sectionId, progress.getFirst().getQuestionNumber()));
	}
	
	@GetMapping("/section/{sectionId}/question/{questionNumber}")
	public String question(Model model, @PathVariable int sectionId, @PathVariable int questionNumber, @SessionAttribute("QUESTIONS_PROGRESS") QuestionProgress progress) {
		if(!progress.isFixedSection(sectionId)) {
			return "redirect:/sections";
		}
		
		Question question = questionFetcher.fetchQuestion(progress, questionNumber);
		
		model.addAttribute("question", question);
		model.addAttribute("progress", progress);
		
		return "question";
	}
	
	@PostMapping("/section/{sectionId}/question/{questionNumber}")
	public String doAnswer(@PathVariable int sectionId, @PathVariable int questionNumber, @RequestParam int answer, HttpSession session, @AuthenticationPrincipal User user) {
		final QuestionProgress progress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		if(progress.getByNumber(questionNumber).isAnswered()) {
			return String.format("redirect:/section/%d/question/%d", sectionId, questionNumber);
		}
		
		boolean isAnswerCorrect = answerChecker.checkAnswer(progress, questionNumber, answer, user);
		Optional<QuestionProgressUnit> nextQuestion = progress.findNextQuestion(questionNumber);
		
		String redirect;
		if(!isAnswerCorrect) {
			if(!nextQuestion.isPresent()) {
				progressCompleteHandler.handle(progress, user);
			}
			redirect = String.format("redirect:/section/%d/question/%d", sectionId, questionNumber);
		}
		else if(nextQuestion.isPresent()) {
			redirect = String.format("redirect:/section/%d/question/%d", sectionId, nextQuestion.get().getQuestionNumber());
		}
		else {
			progressCompleteHandler.handle(progress, user);
			redirect = String.format("redirect:/questions/%s/result", progress.getId());
		}
		
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return redirect;
	}
}
