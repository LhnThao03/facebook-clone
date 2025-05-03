package com.example.facebook_clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private UserService userService;
	@GetMapping
	public String showLoginForm() {
		return "login";
	}
	

	@PostMapping
	public String loginUser(@RequestParam String email,
	                        @RequestParam String password,
	                        HttpSession session,
	                        Model model) {
	    User user = userService.getUserByEmail(email);
	
	    if (user != null && user.getPassword().equals(password)) {
	        session.setAttribute("currentUser", user); // lưu user vào session
	        return "redirect:/home";
	    }
	
	    model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
	    return "login";
	}
}
