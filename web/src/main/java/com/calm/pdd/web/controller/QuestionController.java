package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.web.model.enums.AnswerResult;
import com.calm.pdd.web.model.session.QuestionProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class QuestionController {
	
	private QuestionRepository questionRepository;
	
	public QuestionController(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}
	
	@GetMapping("/section/{sectionId}")
	public RedirectView enterSection(@PathVariable int sectionId, HttpSession session) {
		List<Question> questions = questionRepository.getListBySectionId(sectionId);
		List<QuestionProgress> questionsProgress = questions.stream().map(q -> new QuestionProgress(q.getId(), q.getNumber())).collect(Collectors.toList());
		Question firstQuestion = questions.get(0);
		
		session.setAttribute("QUESTIONS_PROGRESS_LIST", questionsProgress);
		session.setAttribute("CURRENT_SECTION", sectionId);
		
		return new RedirectView(String.format("/section/%d/question/%d", sectionId, firstQuestion.getNumber()));
	}
	
	@GetMapping("/section/{sectionId}/question/{questionNumber}")
	public ModelAndView question(ModelAndView model, @PathVariable int sectionId, @PathVariable int questionNumber, HttpSession session) {
		if((int)session.getAttribute("CURRENT_SECTION") != sectionId) {
			log.warn("Wrong section!");
			return new ModelAndView("redirect:/sections");
		}
		
		List<QuestionProgress> questionsProgress = (List<QuestionProgress>) session.getAttribute("QUESTIONS_PROGRESS_LIST");
		
		try {
			QuestionProgress currentQuestionProgress = questionsProgress.get(questionNumber - 1);
			Question question = questionRepository.findById(currentQuestionProgress.getQuestionId()).orElseThrow(() -> new RuntimeException(String.format("Question %d not found!", currentQuestionProgress.getQuestionId())));
			
			model.addObject("question", question);
			model.addObject("progress", questionsProgress);
			model.addObject("currentQuestionProgress", currentQuestionProgress);
			model.setViewName("question");
			
			return model;
		}
		catch(IndexOutOfBoundsException e) {
			log.warn("Wrong question number for this section!", e);
			return new ModelAndView("redirect:/sections");
		}
	}
	
	@PostMapping("/section/{sectionId}/question/{questionNumber}")
	public ModelAndView doAnswer(@PathVariable int sectionId, @PathVariable int questionNumber, @RequestParam int answer, HttpSession session) {
		List<QuestionProgress> questionsProgress = (List<QuestionProgress>) session.getAttribute("QUESTIONS_PROGRESS_LIST");
		QuestionProgress currentQuestionProgress = questionsProgress.get(questionNumber - 1);
		
		Question question = questionRepository.findById(currentQuestionProgress.getQuestionId()).orElseThrow(() -> new RuntimeException(String.format("Question %d not found!", currentQuestionProgress.getQuestionId())));
		if(question.getAnswer() == answer) {
			currentQuestionProgress.setAnswerResult(AnswerResult.CORRECT);
			currentQuestionProgress.setAnsweredNumber(answer);
			session.setAttribute("QUESTIONS_PROGRESS_LIST", questionsProgress);
			//если следующий вопрос существует, то редирект, иначе надо вернуть туда же, и можно показать алерт с поздравлением
			return new ModelAndView(String.format("redirect:/section/%d/question/%d", sectionId, questionNumber + 1));
		}
		else{
			currentQuestionProgress.setAnswerResult(AnswerResult.WRONG);
			currentQuestionProgress.setAnsweredNumber(answer);
			session.setAttribute("QUESTIONS_PROGRESS_LIST", questionsProgress);
			return new ModelAndView(String.format("redirect:/section/%d/question/%d", sectionId, questionNumber));
		}
	}
}
