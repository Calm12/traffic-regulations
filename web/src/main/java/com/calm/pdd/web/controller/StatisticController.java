package com.calm.pdd.web.controller;

import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.services.statistic.Statistic;
import com.calm.pdd.web.services.CachedStatisticService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticController {
	
	private final CachedStatisticService cachedStatisticService;
	
	public StatisticController(CachedStatisticService cachedStatisticService) {
		this.cachedStatisticService = cachedStatisticService;
	}
	
	@GetMapping("/statistic")
	public String show(Model model, @AuthenticationPrincipal User user) {
		Statistic statistic = cachedStatisticService.getStatistic(user);
		model.addAttribute("statistic", statistic);
		
		return "statistic";
	}
}
