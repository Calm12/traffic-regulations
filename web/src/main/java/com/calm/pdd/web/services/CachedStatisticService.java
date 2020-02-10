package com.calm.pdd.web.services;

import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.services.statistic.Statistic;
import com.calm.pdd.core.services.statistic.StatisticFetcher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CachedStatisticService {
	
	private final StatisticFetcher statisticFetcher;
	
	public CachedStatisticService(StatisticFetcher statisticFetcher) {
		this.statisticFetcher = statisticFetcher;
	}
	
	@Cacheable(value = "statistic", key = "#user.id")
	public Statistic getStatistic(User user) {
		return statisticFetcher.fetch(user);
	}
}
