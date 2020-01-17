package com.calm.pdd.core.model.dto;

import com.calm.pdd.core.model.validation.annotations.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@PasswordMatches(message = "{user.password.matching.failed}")
public class UserDto {
	
	@Pattern(regexp = "^[a-zA-Z0-9]{4,16}$", message = "{user.login.pattern}")
	private String username;
	
	@Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "{user.email.invalid}")
	private String email;
	
	@Pattern(regexp = "^[a-zA-Z0-9]{6,32}$", message = "{user.password.pattern}")
	private String password;
	private String matchingPassword;
}
