package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.services.statistic.Statistic;
import com.calm.pdd.web.services.CachedStatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StatisticControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private CachedStatisticService cachedStatisticService;
	
	@Mock
	private Statistic statistic;
	
	@BeforeEach
	public void setUp() {
		when(cachedStatisticService.getStatistic(any(User.class))).thenReturn(statistic);
		
		mockMvc = MockMvcBuilders.standaloneSetup(new StatisticController(cachedStatisticService)).build();
	}
	
	@Test
	void successShowAction() throws Exception {
		mockMvc.perform(get("/statistic/"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("statistic"))
				.andExpect(model().attribute("statistic", statistic))
				.andExpect(view().name("statistic"))
				.andReturn();
	}
}
