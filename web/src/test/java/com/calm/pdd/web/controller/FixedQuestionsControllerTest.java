package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import com.calm.pdd.core.model.session.Result;
import com.calm.pdd.core.services.AnswerChecker;
import com.calm.pdd.core.services.QuestionFetcher;
import com.calm.pdd.core.services.ResultCollector;
import com.calm.pdd.core.services.SectionFetcher;
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
public class FixedQuestionsControllerTest {
	
	private MockMvc mockMvc;
	
	private MockHttpSession session;
	
	@Mock
	private SectionFetcher sectionFetcher;
	
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
		
		mockMvc = MockMvcBuilders.standaloneSetup(new FixedQuestionsController(sectionFetcher, questionFetcher, answerChecker, resultCollector))
				.setControllerAdvice(BaseExceptionHandler.class)
				.build();
	}
	
	@Test
	void successEnterSectionAction() throws Exception {
		when(sectionFetcher.fetchSection(10)).thenReturn(questionProgress);
		when(questionProgress.getFirst()).thenReturn(questionProgressUnit);
		when(questionProgressUnit.getQuestionNumber()).thenReturn(1);
		
		mockMvc.perform(get("/section/10/").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/section/10/question/1"))
				.andReturn();
		
		assertThat(session.getAttribute("QUESTIONS_PROGRESS")).isEqualTo(questionProgress);
	}
	
	@Test
	void successShowQuestionAction() throws Exception {
		when(questionFetcher.fetchQuestion(questionProgress, 1)).thenReturn(question);
		when(questionProgress.isFixedSection(10)).thenReturn(true);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(get("/section/10/question/1/").session(session))
				.andExpect(status().isOk())
				.andExpect(model().attribute("question", question))
				.andExpect(model().attribute("progress", questionProgress))
				.andExpect(view().name("question"))
				.andReturn();
	}
	
	@Test
	void showQuestionActionWithWrongSectionInProgress() throws Exception {
		when(questionProgress.isFixedSection(10)).thenReturn(false);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(get("/section/10/question/1/").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/sections"))
				.andReturn();
	}
	
	@Test
	void doAnswerCorrectActionAndHasNextQuestion() throws Exception {
		when(answerChecker.checkAnswer(eq(questionProgress), eq(1), eq(3), any(User.class))).thenReturn(true);
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.of(nextQuestionProgressUnit));
		when(nextQuestionProgressUnit.getQuestionNumber()).thenReturn(2);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/section/10/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/section/10/question/2"))
				.andReturn();
		
		verify(questionProgress, never()).setResult(any());
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
	
	@Test
	void doAnswerCorrectActionAndHasNotNextQuestion() throws Exception {
		when(answerChecker.checkAnswer(eq(questionProgress), eq(1), eq(3), any(User.class))).thenReturn(true);
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.empty());
		when(questionProgress.getId()).thenReturn("1234567");
		when(resultCollector.collect(questionProgress)).thenReturn(result);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/section/10/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/questions/1234567/complete"))
				.andReturn();
		
		verify(questionProgress).setResult(result);
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
	
	@Test
	void doAnswerWrongActionAndHasNextQuestion() throws Exception {
		when(answerChecker.checkAnswer(eq(questionProgress), eq(1), eq(3), any(User.class))).thenReturn(false);
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.of(nextQuestionProgressUnit));
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/section/10/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/section/10/question/1"))
				.andReturn();
		
		verify(questionProgress, never()).setResult(any());
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
	
	@Test
	void doAnswerWrongActionAndHasNotNextQuestion() throws Exception {
		when(answerChecker.checkAnswer(eq(questionProgress), eq(1), eq(3), any(User.class))).thenReturn(false);
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.empty());
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/section/10/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/section/10/question/1"))
				.andReturn();
		
		verify(questionProgress).setResult(any());
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
}
