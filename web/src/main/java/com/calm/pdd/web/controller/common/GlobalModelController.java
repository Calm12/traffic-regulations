package com.calm.pdd.web.controller.common;

import com.calm.pdd.core.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
public class GlobalModelController {
	
	@ModelAttribute("authUser")
	public User populateUser(@AuthenticationPrincipal User user) {
		return user;
	}
}
