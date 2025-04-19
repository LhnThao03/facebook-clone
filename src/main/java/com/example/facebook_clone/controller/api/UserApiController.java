package com.example.facebook_clone.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
	@Autowired
	private UserService userService;
	@GetMapping
	List<User> getProduct(){
		return userService.getUsers();
	}
}
