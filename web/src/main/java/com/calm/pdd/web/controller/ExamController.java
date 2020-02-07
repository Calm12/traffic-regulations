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
public class ExamController {
	
	private ExamSetFetcher examSetFetcher;
	private QuestionFetcher questionFetcher;
	private AnswerChecker answerChecker;
	private ProgressCompleteHandler progressCompleteHandler;
	
	public ExamController(ExamSetFetcher examSetFetcher, QuestionFetcher questionFetcher, AnswerChecker answerChecker, ProgressCompleteHandler progressCompleteHandler) {
		this.examSetFetcher = examSetFetcher;
		this.questionFetcher = questionFetcher;
		this.answerChecker = answerChecker;
		this.progressCompleteHandler = progressCompleteHandler;
	}
	
	@GetMapping("/exam")
	public RedirectView enterSection(HttpSession session) {
		QuestionProgress progress = examSetFetcher.fetchSet();
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return new RedirectView(String.format("/exam/question/%d", progress.getFirst().getQuestionNumber()));
	}
	
	@GetMapping("/exam/question/{questionNumber}")
	public String question(Model model, @PathVariable int questionNumber, @SessionAttribute("QUESTIONS_PROGRESS") QuestionProgress progress) {
		if(!progress.isExam()) {
			return "redirect:/sections";
		}
		
		Question question = questionFetcher.fetchQuestion(progress, questionNumber);
		
		model.addAttribute("question", question);
		model.addAttribute("currentProgressUnit", progress.getByNumber(questionNumber));
		model.addAttribute("progress", progress);
		
		return "question";
	}
	
	@PostMapping("/exam/question/{questionNumber}")
	public String doAnswer(@PathVariable int questionNumber, @RequestParam int answer, HttpSession session, @AuthenticationPrincipal User user) {
		final QuestionProgress progress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		if(progress.isCompleted()) {
			return String.format("redirect:/exam/question/%d", questionNumber);
		}
		
		if(progress.getByNumber(questionNumber).isAnswered()) {
			return String.format("redirect:/exam/question/%d", questionNumber);
		}
		
		answerChecker.checkAnswer(progress, questionNumber, answer, user);
		Optional<QuestionProgressUnit> nextQuestion = progress.findNextQuestion(questionNumber);
		
		String redirect;
		if(progress.hasTwoErrors()) {
			progressCompleteHandler.handle(progress, user);
			redirect = String.format("redirect:/questions/%s/result", progress.getId());
		}
		else if(nextQuestion.isPresent()) {
			redirect = String.format("redirect:/exam/question/%d", nextQuestion.get().getQuestionNumber());
		}
		else {
			progressCompleteHandler.handle(progress, user);
			redirect = String.format("redirect:/questions/%s/result", progress.getId());
		}
		
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return redirect;
	}
}
