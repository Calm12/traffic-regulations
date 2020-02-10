package com.calm.pdd.core.exceptions;

import com.calm.pdd.core.model.entity.User;

public class UserHasNotStatisticException extends RuntimeException {
	
	public UserHasNotStatisticException(User user) {
		super("Wtf?! User [" + user.getId() + "] has not UserStatistic!");
	}
}
