package com.calm.pdd.parsers.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Data
public class AppConfig {
	
	private Parsers parsers;
	
	@Data
	public static class Parsers {
		private String url;
		private String userAuthCookie;
	}
}
