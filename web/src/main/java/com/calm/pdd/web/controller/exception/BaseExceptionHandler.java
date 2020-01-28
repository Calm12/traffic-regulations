package com.calm.pdd.web.controller.exception;

import com.calm.pdd.core.exceptions.UnknownQuestionException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class BaseExceptionHandler {
	
	@ExceptionHandler(UnknownQuestionException.class)
	public RedirectView unknownQuestionException(UnknownQuestionException e) {
		return new RedirectView("/sections");
	}
	
	@ExceptionHandler(ServletRequestBindingException.class)
	public RedirectView servletRequestBindingException(RedirectAttributes flash, ServletRequestBindingException e) {
		flash.addFlashAttribute("flash", "Ваша сессия истекла");
		
		return new RedirectView("/sections");
	}
}
