package com.example.facebook_clone.controller;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.facebook_clone.dto.PostDTO;
import com.example.facebook_clone.model.User;
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
	public String home(HttpSession session, Model model) {
	    User currentUser = (User) session.getAttribute("currentUser");
	    List<User> suggestions = userService.getFriendSuggestions(currentUser);
	    List<PostDTO> postsWithInteraction = postService.getAllPostsWithInteractions(currentUser.getUserId());
	    model.addAttribute("postsWithInteraction", postsWithInteraction);

	    model.addAttribute("posts", postService.getLatestPosts());
	    model.addAttribute("suggestions", suggestions);
	    model.addAttribute("currentUser", currentUser);

	    return "home";
	}
}
