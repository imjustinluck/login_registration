package com.jda.authentication.validators;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jda.authentication.models.User;
import com.jda.authentication.services.UserService;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService userService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		if(!user.getPasswordConfirmation().equals(user.getPassword())) {
			errors.rejectValue("passwordConfirmation", "Match");
		}
		
//		String email = user.getEmail();
//		User foundUser = userService.findByEmail(email);
//		
//		System.out.println(foundUser);
//		System.out.println(foundUser.getEmail());
//		
//		if(!user.getEmail().equals(foundUser.getEmail())) {
//			errors.rejectValue("email", "Email");
//			System.out.println("Hello");
//		}

	}

}
