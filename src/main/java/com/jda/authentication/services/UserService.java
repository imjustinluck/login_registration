package com.jda.authentication.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jda.authentication.models.User;
import com.jda.authentication.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
    
    // Register User and Hash Password
    public User registerUser(User user) {
    	String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    	user.setPassword(hashed);
    	return userRepository.save(user);
    }
    
    // Get One User By Email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    // Get One User By ID
    public User findUserById(Long id) {
    	Optional<User> user = userRepository.findById(id);	
    	if(user.isPresent()) {
            return user.get();
    	} else {
    	    return null;
    	}
    }
    
    // Authentication
    public boolean authenticateUser(String email, String password) {
//    	Finds User by Email
    	User user = userRepository.findByEmail(email);
//    	Return False if Email Doesn't Exist
    	if(user == null) {
    		return false;
    	}
//    	Checks if Passwords Match and Returns Boolean
    	else {
    		if(BCrypt.checkpw(password, user.getPassword())) {
    			return true;
    		}
    		else {
    			return false;
    		}
    	}
    }
}
