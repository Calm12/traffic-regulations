package com.calm.pdd.web.controller;

import com.calm.pdd.web.controller.common.GlobalModelController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//https://docs.spring.io/spring/docs/5.2.x/spring-framework-reference/testing.html#spring-mvc-test-framework
//@WebMvcTest(HomeController.class) это нужно для автоподнятия контекста, а я вручную создаю mockmvc и передаю туда контроллер
//@TestPropertySource("/application-test.properties") тоже нужно только если поднимать контекст
public class HomeControllerTest {
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new HomeController())
				.setControllerAdvice(new GlobalModelController())
				.build();
	}
	
	@Test
	void indexAction() throws Exception {
		mockMvc.perform(get("/"))
				//.with(userHttpBasic(ADMIN))) //пример авторизации?
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("authUser"))
				.andExpect(view().name("index"))
				.andReturn();
	}
	
}
