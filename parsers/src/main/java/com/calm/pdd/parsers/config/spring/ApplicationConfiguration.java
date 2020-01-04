package com.calm.pdd.parsers.config.spring;

import com.calm.pdd.parsers.config.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfiguration {
	
	@Bean(name = "cookies")
	public Map<String, String> getCookies(AppConfig appConfig) {
		Map<String, String> cookies = new HashMap<>();
		cookies.put("cookieWarned", "true");
		cookies.put("user_auth", appConfig.getParsers().getUserAuthCookie());
		
		return cookies;
	}
}
