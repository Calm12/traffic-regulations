package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
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
public class ExamControllerTest {
	
	private MockMvc mockMvc;
	
	private MockHttpSession session;
	
	@Mock
	private ExamSetFetcher examSetFetcher;
	
	@Mock
	private QuestionFetcher questionFetcher;
	
	@Mock
	private AnswerChecker answerChecker;
	
	@Mock
	private ProgressCompleteHandler progressCompleteHandler;

	@Mock
	private QuestionProgress questionProgress;
	
	@Mock
	private QuestionProgressUnit questionProgressUnit;
	
	@Mock
	private QuestionProgressUnit nextQuestionProgressUnit;
	
	@Mock
	private Question question;
	
	@BeforeEach
	public void setUp() {
		session = spy(new MockHttpSession());
		lenient().when(questionProgress.getByNumber(1)).thenReturn(questionProgressUnit);
		
		mockMvc = MockMvcBuilders.standaloneSetup(new ExamController(examSetFetcher, questionFetcher, answerChecker, progressCompleteHandler))
				.setControllerAdvice(BaseExceptionHandler.class)
				.build();
	}
	
	@Test
	void successEnterSectionAction() throws Exception {
		when(examSetFetcher.fetchSet()).thenReturn(questionProgress);
		when(questionProgress.getFirst()).thenReturn(questionProgressUnit);
		when(questionProgressUnit.getQuestionNumber()).thenReturn(1);
		
		mockMvc.perform(get("/exam").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/exam/question/1"))
				.andReturn();
		
		assertThat(session.getAttribute("QUESTIONS_PROGRESS")).isEqualTo(questionProgress);
	}
	
	@Test
	void successShowQuestionAction() throws Exception {
		when(questionFetcher.fetchQuestion(questionProgress, 1)).thenReturn(question);
		when(questionProgress.getByNumber(1)).thenReturn(questionProgressUnit);
		when(questionProgress.isExam()).thenReturn(true);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(get("/exam/question/1/").session(session))
				.andExpect(status().isOk())
				.andExpect(model().attribute("question", question))
				.andExpect(model().attribute("currentProgressUnit", questionProgressUnit))
				.andExpect(model().attribute("progress", questionProgress))
				.andExpect(view().name("question"))
				.andReturn();
	}
	
	@Test
	void showQuestionActionWithWrongSectionInProgress() throws Exception {
		when(questionProgress.isExam()).thenReturn(false);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(get("/exam/question/1/").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/sections"))
				.andReturn();
	}
	
	@Test
	void doAnswerActionAndHasNextQuestion() throws Exception {
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.of(nextQuestionProgressUnit));
		when(nextQuestionProgressUnit.getQuestionNumber()).thenReturn(2);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/exam/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/exam/question/2"))
				.andReturn();
		
		verify(progressCompleteHandler, never()).handle(eq(questionProgress), any(User.class));
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
	
	@Test
	void doAnswerActionAndHasNotNextQuestion() throws Exception {
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.empty());
		when(questionProgress.getId()).thenReturn("1234567");
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/exam/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/questions/1234567/result"))
				.andReturn();
		
		verify(progressCompleteHandler).handle(eq(questionProgress), any(User.class));
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
	
	@Test
	void doAnswerActionAndHasTwoErrorsNow() throws Exception {
		when(questionProgress.findNextQuestion(1)).thenReturn(Optional.of(nextQuestionProgressUnit));
		when(questionProgress.getId()).thenReturn("1234567");
		when(questionProgress.hasTwoErrors()).thenReturn(true);
		
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc.perform(post("/exam/question/1/").param("answer", "3").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/questions/1234567/result"))
				.andReturn();
		
		verify(progressCompleteHandler).handle(eq(questionProgress), any(User.class));
		verify(session, times(2)).setAttribute("QUESTIONS_PROGRESS", questionProgress);
	}
}
