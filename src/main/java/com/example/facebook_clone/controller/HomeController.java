package com.example.facebook_clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.facebook_clone.service.PostService;
import com.example.facebook_clone.service.UserService;

@Controller
@RequestMapping("/home")
public class HomeController {
	@Autowired 
	private PostService postService;
	@Autowired
	private UserService userService;

    @GetMapping
    public String home(Model model) {
        Integer currentUserId = 1; // Giả lập user đang đăng nhập
        model.addAttribute("posts", postService.getLatestPosts());
        model.addAttribute("suggestions", userService.getFriendSuggestions(currentUserId));
        return "home"; // home.html
    }
}
