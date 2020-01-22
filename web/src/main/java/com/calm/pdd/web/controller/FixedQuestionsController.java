package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import com.calm.pdd.core.services.*;
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
	private ResultCollector resultCollector;
	
	public FixedQuestionsController(SectionFetcher sectionFetcher, QuestionFetcher questionFetcher, AnswerChecker answerChecker, ResultCollector resultCollector) {
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
	public String question(Model model, @PathVariable int sectionId, @PathVariable int questionNumber, @SessionAttribute("QUESTIONS_PROGRESS") QuestionProgress progress) {
		if(progress == null) {
			return "redirect:/sections";
		}
		
		if(!progress.isFixedSection(sectionId)) {
			return "redirect:/sections";
		}
		
		Question question = questionFetcher.fetchQuestion(progress, questionNumber);
		
		model.addAttribute("question", question);
		model.addAttribute("progress", progress);
		
		return "question";
	}
	
	@PostMapping("/section/{sectionId}/question/{questionNumber}")
	public String doAnswer(@PathVariable int sectionId, @PathVariable int questionNumber, @RequestParam int answer, HttpSession session) {
		final QuestionProgress progress = (QuestionProgress) session.getAttribute("QUESTIONS_PROGRESS");
		
		boolean isAnswerCorrect = answerChecker.checkAnswer(progress, questionNumber, answer);
		Optional<QuestionProgressUnit> nextQuestion = progress.findNextQuestion(questionNumber);
		
		String redirect;
		if(!isAnswerCorrect) {
			if(!nextQuestion.isPresent()) {
				progress.setResult(resultCollector.collect(progress));
			}
			redirect = String.format("redirect:/section/%d/question/%d", sectionId, questionNumber);
		}
		else if(nextQuestion.isPresent()) {
			redirect = String.format("redirect:/section/%d/question/%d", sectionId, nextQuestion.get().getQuestionNumber());
		}
		else {
			progress.setResult(resultCollector.collect(progress));
			redirect = String.format("redirect:/questions/%s/complete", progress.getId());
		}
		
		session.setAttribute("QUESTIONS_PROGRESS", progress);
		
		return redirect;
	}
}
