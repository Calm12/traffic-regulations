package com.calm.pdd.web.controller.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String login(Model model, HttpSession session) {
		AuthenticationException loginException = (AuthenticationException) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		if(loginException != null) {
			session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
			model.addAttribute("loginError", loginException.getMessage());
		}
		
		return "login";
	}
}
