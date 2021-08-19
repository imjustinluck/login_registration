package com.jda.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jda.authentication.models.User;
import com.jda.authentication.services.UserService;
import com.jda.authentication.validators.UserValidator;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator validator;
	
//	Registration Routes
	@GetMapping("/registration")
	public String registerForm(@ModelAttribute("user") User user) {
		return "registrationPage.jsp";
	}
	@PostMapping(value="/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user,
    		BindingResult result, HttpSession session) {
		validator.validate(user, result);
		if(result.hasErrors()) {
			return "registrationPage.jsp";
		}
		else {
			User newUser = userService.registerUser(user);
			session.setAttribute("user_id", newUser.getId());
			return "redirect:/home";
		}
	}
	
//	Login Routes
	@RequestMapping("/login")
	public String login() {
		return "loginPage.jsp";
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginUser(@RequestParam("email") String email,
			@RequestParam("password") String password,
			Model model, HttpSession session, RedirectAttributes flash) {
		// User Authenticated
		if(userService.authenticateUser(email, password)) {
			User user = userService.findByEmail(email);
			session.setAttribute("user_id", user.getId());
			return "redirect:/home";
		}
		// Add Errors, Return To Login
		else {
			flash.addFlashAttribute("error", "Invalid email / password combination");			
			return "redirect:/login";
		}
	}

//	Home
	@RequestMapping("/home")
	public String home(HttpSession session, Model model) {
		// get user from session, save them in the model and return the home page
		Long id = (Long) session.getAttribute("user_id");
		if(id != null) {
			User user = userService.findUserById(id);
			model.addAttribute(user);
			return "homePage.jsp";			
		}
		else {
			return "redirect:/login";
		}
	}

//	Logout
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}
