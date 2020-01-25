package com.calm.pdd.core.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDtoValidationTest {
	
	private UserDto userDto;
	
	private Validator validator;
	
	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		
		userDto = new UserDto()
				.setUsername("john")
				.setEmail("john@gmail.com")
				.setPassword("123456")
				.setMatchingPassword("123456");
	}
	
	@Test
	void successValidation() {
		Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
		assertThat(violations.isEmpty()).isTrue();
	}
	
	@Test
	void validationFailedOnUsername() {
		userDto.setUsername("x");
		
		Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("{user.login.pattern}");
	}
	
	@Test
	void validationFailedOnEmail() {
		userDto.setEmail("x");
		
		Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("{user.email.invalid}");
	}
	
	@Test
	void validationFailedOnPassword() {
		userDto.setPassword("xxx");
		userDto.setMatchingPassword("xxx");
		
		Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("{user.password.pattern}");
	}
	
	@Test
	void validationFailedOnPasswordMatching() {
		userDto.setMatchingPassword("xxx");
		
		Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("{user.password.matching.failed}");
	}
}
