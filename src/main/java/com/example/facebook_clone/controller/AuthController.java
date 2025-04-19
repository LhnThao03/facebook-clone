package com.example.facebook_clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.facebook_clone.dto.request.UserCreationRequest;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.model.User.Gender;
import com.example.facebook_clone.model.User.Role;
import com.example.facebook_clone.service.UserService;

@Controller
@RequestMapping("/register")
public class AuthController {
	@Autowired
    private UserService userService;

	@PostMapping
	public String createUser(
		@RequestParam String firstname,
		@RequestParam String lastname,
		@RequestParam String password, 
		@RequestParam String phone, 
		@RequestParam String email, 
		@RequestParam(defaultValue = "other") Gender gender,
		@RequestParam(defaultValue = "user") Role role,
		Model model) {
			UserCreationRequest request = new UserCreationRequest();
			request.setFirstname(firstname);
			request.setLastname(lastname);
			request.setPassword(password);
			request.setPhone(phone);
			request.setEmail(email);
			request.setGender(gender);
			request.setRole(role);
			
			userService.createUser(request);
			model.addAttribute("users", userService.getUsers());
			return "redirect:/login";
	}
	
	@GetMapping
	public String showRegisterForm() {
	    return "register"; // Tên file register.html
	}
}
