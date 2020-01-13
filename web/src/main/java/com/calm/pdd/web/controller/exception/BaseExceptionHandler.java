package com.calm.pdd.web.controller.exception;

import com.calm.pdd.core.exceptions.UnknownQuestionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class BaseExceptionHandler {
	
	@ExceptionHandler(UnknownQuestionException.class)
	public RedirectView unknownQuestionException(UnknownQuestionException e) {
		return new RedirectView("/sections");
	}
}
