package com.example.facebook_clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.facebook_clone.dto.request.UserCreationRequest;
import com.example.facebook_clone.model.Profile;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.model.User.Gender;
import com.example.facebook_clone.model.User.Role;
import com.example.facebook_clone.service.ProfileService;
import com.example.facebook_clone.service.UserService;

@Controller
@RequestMapping("/register")
public class AuthController {
	@Autowired
    private UserService userService;
	@Autowired
    private ProfileService profileService;

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
			
			
			// Tạo user
		    User user = userService.createUser(request);
			// Tạo Profile cho user vừa tạo
		    Profile profile = new Profile();
		    profile.setUser(user);  // Liên kết profile với user
		    profile.setBio(""); // Hoặc bạn có thể để trống hoặc thêm thông tin mặc định
		    profile.setLocation(""); // Thêm location mặc định nếu cần
		    profile.setBirthdate(null); // Hoặc gán giá trị ngày sinh mặc định
		    profile.setCoverPicture(""); // Thêm ảnh bìa mặc định nếu cần
		    // Lưu profile vào database
		    profileService.createProfile(profile); // Giả sử bạn có một service để lưu Profile
			model.addAttribute("users", userService.getUsers());
			return "redirect:/login";
	}
	
	@GetMapping
	public String showRegisterForm() {
	    return "register"; // Tên file register.html
	}
}
