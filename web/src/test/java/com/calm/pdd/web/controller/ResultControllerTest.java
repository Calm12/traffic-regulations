package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.session.QuestionProgress;
import com.calm.pdd.web.controller.exception.BaseExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ResultControllerTest {
	
	private MockMvc mockMvc;
	
	private MockHttpSession session;
	
	@Mock
	private QuestionProgress questionProgress;
	
	@BeforeEach
	public void setUp() {
		lenient().when(questionProgress.getId()).thenReturn("12345");
		
		session = new MockHttpSession();
		session.setAttribute("QUESTIONS_PROGRESS", questionProgress);
		
		mockMvc = MockMvcBuilders.standaloneSetup(new ResultController())
				.setControllerAdvice(BaseExceptionHandler.class)
				.build();
	}
	
	@Test
	void successCompleteAction() throws Exception {
		mockMvc.perform(get("/questions/12345/complete/").session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("complete"))
				.andReturn();
	}
	
	@Test
	void completeActionWithWrongProgressId() throws Exception {
		mockMvc.perform(get("/questions/123456/complete/").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/sections"))
				.andReturn();
	}
	
	@Test
	void completeActionWithEmptySession() throws Exception {
		session.removeAttribute("QUESTIONS_PROGRESS");
		
		mockMvc.perform(get("/questions/12345/complete/").session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/sections"))
				.andReturn();
	}
}
