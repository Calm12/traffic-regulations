package com.calm.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfiguration {
	
	@Bean(name = "cookies")
	public Map<String, String> getCookies() {
		Map<String, String> cookies = new HashMap<>();
		cookies.put("cookieWarned", "true");
		cookies.put("user_auth", "eyJpdiI6IlwvY1NDT0l1RWJieGd6a1NsOHN5c1p3PT0iLCJ2YWx1ZSI6ImJ1YnpwTEd1d292RkUrOTB1V2kyRklyd2hpQUZ0OXlUVSs4a1UyV1B2Qk8rcis4NEM5Ukpvd3QrVEpqMnI4Mm1VbjFMREJXQXVvakNVOFVCdkV3N1ZqNmJoa3hLcmh1SVB0dnRhMlg2YmhFek5cL2syNnVJb3B0YVIrMlpIQUU0diIsIm1hYyI6ImU3ODUyMjlhZTY4NDQ5MzRjMWIzMDI2NDNjODg4YzdlM2M0MGNmMWYzOWE0ZWRlODE3MGZiN2Q5MGVjNWM2NWEifQ%3D%3D");
		
		return cookies;
	}
}
