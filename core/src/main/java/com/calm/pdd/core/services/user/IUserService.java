package com.calm.pdd.core.services.user;

import com.calm.pdd.core.exceptions.EmailExistsException;
import com.calm.pdd.core.exceptions.UserExistsException;
import com.calm.pdd.core.model.dto.UserDto;
import com.calm.pdd.core.model.entity.User;

public interface IUserService {
	
	User registerNewUser(UserDto userDto) throws UserExistsException, EmailExistsException;
}
