package com.ai.jwd42.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ai.jwd42.dto.User;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors error) {
		// TODO Auto-generated method stub
		ValidationUtils.rejectIfEmptyOrWhitespace(error,"name", "required.name","Field name is required!");

	}

}
