package com.example.facebook_clone.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
	    @RequestParam int birthDay,
	    @RequestParam int birthMonth,
	    @RequestParam int birthYear,
	    Model model
	) {
	    // Kiểm tra ngày sinh trước
	    LocalDateTime birthdate;
	    try {
	        birthdate = LocalDate.of(birthYear, birthMonth, birthDay).atStartOfDay();
	    } catch (DateTimeException e) {
	        model.addAttribute("error", "Ngày sinh không hợp lệ. Vui lòng chọn lại.");
	        return "register";  // Trả về form đăng ký
	    }

	    // Sau khi hợp lệ mới tiếp tục tạo user
	    UserCreationRequest request = new UserCreationRequest();
	    request.setFirstname(firstname);
	    request.setLastname(lastname);
	    request.setPassword(password);
	    request.setPhone(phone);
	    request.setEmail(email);
	    request.setGender(gender);
	    request.setRole(role);

	    User user = userService.createUser(request);

	    Profile profile = new Profile();
	    profile.setUser(user);
	    profile.setBio("");
	    profile.setLocation("");
	    profile.setBirthdate(birthdate);
	    profile.setCoverPicture("");

	    profileService.createProfile(profile);
	    return "redirect:/login";
	}
	
	@GetMapping
	public String showRegisterForm() {
	    return "register"; // Tên file register.html
	}
}
