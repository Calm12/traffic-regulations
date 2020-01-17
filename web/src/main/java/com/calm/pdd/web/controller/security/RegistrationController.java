package com.calm.pdd.web.controller.security;

import com.calm.pdd.core.exceptions.EmailExistsException;
import com.calm.pdd.core.exceptions.UserExistsException;
import com.calm.pdd.core.model.dto.UserDto;
import com.calm.pdd.core.services.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
	
	private UserService userService;
	
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/reg")
	public String registrationPage(Model model) {
		model.addAttribute("user", new UserDto());
		
		return "reg";
	}
	
	@PostMapping("/reg")
	public String register(@ModelAttribute("user") @Valid final UserDto userDto, BindingResult result, Errors errors, Model model) {
		if (result.hasErrors()) {
			return "reg";
		}
		
		try {
			userService.registerNewUser(userDto);
			
			return "redirect:/login";
		}
		catch(UserExistsException e) {
			errors.rejectValue("username", "user.login.exists");
		}
		catch(EmailExistsException e) {
			errors.rejectValue("email", "user.email.exists");
		}
		
		return "reg";
	}
}
