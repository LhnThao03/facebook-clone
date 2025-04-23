package com.example.facebook_clone.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.facebook_clone.dto.PostDTO;
import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.Profile;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.PostService;
import com.example.facebook_clone.service.ProfileService;
import com.example.facebook_clone.service.UserService;

import jakarta.servlet.http.HttpSession;

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
        List<PostDTO> posts = postService.getPostsByUserId(id);
        int friendCount = userService.countFriends(user);

        model.addAttribute("user", user);
        model.addAttribute("profile",profile);
        model.addAttribute("posts", posts);
        model.addAttribute("friendCount", friendCount);

        return "profile"; // trỏ tới file profile.html
    }
	
	@PostMapping("{id}")
	@ResponseBody
	public ResponseEntity<String> updateBio(@RequestParam("bio") String bio, HttpSession session) {
	    User currentUser = (User) session.getAttribute("currentUser");

	    profileService.updateBio(currentUser.getUserId(), bio);
	    return ResponseEntity.ok("Bio updated successfully");
	}
	
	@PostMapping("{id}/update-details")
	public String updateDetails(@PathVariable("id") int userId,
	                            @RequestParam String location,
	                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdate) {
	    profileService.updateLocationAndBirthdate(userId, location, birthdate);
	    return "redirect:/profile/" + userId;
	}

}
