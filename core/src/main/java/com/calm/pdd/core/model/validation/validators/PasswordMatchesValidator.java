package com.calm.pdd.core.model.validation.validators;

import com.calm.pdd.core.model.dto.UserDto;
import com.calm.pdd.core.model.validation.annotations.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
	
	@Override
	public void initialize(final PasswordMatches constraintAnnotation) {
		//
	}
	
	@Override
	public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
		final UserDto user = (UserDto) obj;
		return user.getPassword().equals(user.getMatchingPassword());
	}
	
}