package com.example.facebook_clone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.Profile;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.PostService;
import com.example.facebook_clone.service.ProfileService;
import com.example.facebook_clone.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	@Autowired
	private UserService userService;
	@Autowired
    private PostService postService;
	@Autowired
	private ProfileService profileService;
	
	@GetMapping("{id}")
    public String getUserProfile(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id);
        Profile profile = profileService.getProfileByUserId(id);
        List<Post> posts = postService.getPostsByUserId(id);
        int friendCount = userService.countFriends(user);

        model.addAttribute("user", user);
        model.addAttribute("profile",profile);
        model.addAttribute("posts", posts);
        model.addAttribute("friendCount", friendCount);

        return "profile"; // trỏ tới file profile.html
    }
}
