package com.calm.controller;

import com.calm.model.entity.Question;
import com.calm.model.repository.QuestionRepository;
import com.calm.model.session.QuestionProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
			int questionId = questionsProgress.get(questionNumber - 1).getQuestionId();
			Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException(String.format("Question %d not found!", questionId)));
			
			model.addObject("question", question);
			model.addObject("progress", questionsProgress);
			model.setViewName("question");
			
			return model;
		}
		catch(IndexOutOfBoundsException e) {
			log.warn("Wrong question number for this section!", e);
			return new ModelAndView("redirect:/sections");
		}
	}
	
	@PostMapping("/section/{sectionId}/question/{questionNumber}")
	public ModelAndView doAnswer(ModelAndView model, @PathVariable int sectionId, @PathVariable int questionNumber, HttpSession session) {
		List<QuestionProgress> questionsProgress = (List<QuestionProgress>) session.getAttribute("QUESTIONS_PROGRESS_LIST");
		
		return model;
	}
}
