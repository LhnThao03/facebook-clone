package com.example.facebook_clone.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.facebook_clone.dto.request.UserCreationRequest;
import com.example.facebook_clone.dto.request.UserUpdateRequest;
import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.PostService;
import com.example.facebook_clone.service.UserService;
import com.example.facebook_clone.model.User.Role;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	
//	@GetMapping
//	public String getUsers(Model model){
//		List<User> allUsers = userService.getUsers();
//		List<User> topUsers = userService.getTop10Users();
//		model.addAttribute("allUsers", allUsers);
//		model.addAttribute("topUsers", topUsers);
//		return "admin/dashboard";
//	}
	
	//Phân trang
	@GetMapping
    public String getData(@RequestParam(defaultValue = "0") int page, Model model) {
		// Load du lieu
        Page<User> userPage = userService.getUsers(page, 10); // Lấy 10 user trên mỗi trang
        List<User> topUsers = userService.getTop10Users();
        Page<Post> postPage = postService.getPosts(page, 10);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userPage", userPage);
        model.addAttribute("topUsers", topUsers);
        //Thong ke
        long totalUsers = userService.getTotalUsers();
        long postsToday = postService.countPostsToday();
        model.addAttribute("postsToday", postsToday);
        model.addAttribute("totalUsers", totalUsers);
        
        return "admin/dashboard";
    }
}
