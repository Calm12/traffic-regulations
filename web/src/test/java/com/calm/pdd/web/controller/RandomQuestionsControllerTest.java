package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import com.calm.pdd.core.model.session.Result;
import com.calm.pdd.core.services.*;
import com.calm.pdd.web.controller.exception.BaseExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RandomQuestionsControllerTest {
	
	private MockMvc mockMvc;
	
	private MockHttpSession session;
	
	@Mock
	private RandomSetFetcher randomSetFetcher;
	
	@Mock
	private QuestionFetcher questionFetcher;
	
	@Mock
	private AnswerChecker answerChecker;
	
	@Mock
	private ResultCollector resultCollector;

	@Mock
	private QuestionProgress questionProgress;
	
	@Mock
	private QuestionProgressUnit questionProgressUnit;
	
	@Mock
	private QuestionProgressUnit nextQuestionProgressUnit;
	
	@Mock
	private Question question;
	
	@Mock
	private Result result;
	
	@BeforeEach
	public void setUp() {
		session = spy(new MockHttpSession());
		
		mockMvc = MockMvcBuilders.standaloneSetup(new RandomQuestionsController(randomSetFetcher, questionFetcher, answerChecker, resultCollector))
				.setControllerAdvice(BaseExceptionHandler.class)
				.build();
	}
	
	@Test
	void successEnterSectionAction() throws Exception {
		when(randomSetFetcher.fetchSet()).thenReturn(questionProgress);
		when(questionProgress.getFirst()).thenReturn(questionProgressUnit);
		when(questionProgressUnit.getQuestionNumber()).thenReturn(1);
		
		mockMvc.perform(get("/random").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/random/question/1"))
				.andReturn();
		
		assertThat(session.getAttribute("QUESTIONS_PROGRESS")).isEqualTo(questionProgress);
	}
	
	@Test
	void successShowQuestionAction() throws Exception {
		when(questionFetcher.fetchQuestion(questionProgress, 1)).thenReturn(question);
		when(questionProgress.getByNumber(1)).thenReturn(questionProgressUnit);
		when(questionProgress.isRandomSet()).thenReturn(true);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(get("/random/question/1/").session(session))
				.andExpect(status().isOk())
				.andExpect(model().attribute("question", question))
				.andExpect(model().attribute("currentProgressUnit", questionProgressUnit))
				.andExpect(model().attribute("progress", questionProgress))
				.andExpect(view().name("question"))
				.andReturn();
	}
	
	@Test
	void showQuestionActionWithWrongSectionInProgress() throws Exception {
		when(questionProgress.isRandomSet()).thenReturn(false);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(get("/random/question/1/").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/sections"))
				.andReturn();
	}
	
	@Test
	void doAnswerActionAndHasNextQuestion() throws Exception {
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.of(nextQuestionProgressUnit));
		when(nextQuestionProgressUnit.getQuestionNumber()).thenReturn(2);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/random/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/random/question/2"))
				.andReturn();
		
		verify(questionProgress, never()).setResult(any());
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
	
	@Test
	void doAnswerActionAndHasNotNextQuestion() throws Exception {
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.empty());
		when(questionProgress.getId()).thenReturn("1234567");
		when(resultCollector.collect(questionProgress)).thenReturn(result);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/random/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/questions/1234567/complete"))
				.andReturn();
		
		verify(questionProgress).setResult(result);
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
}
