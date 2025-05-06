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
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.UserService;

import jakarta.servlet.http.HttpSession;

import com.example.facebook_clone.model.User.Role;

@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/{id}")
	public String getUserDetail(@PathVariable Integer id, Model model) {
		User user = userService.getUserById(id);
	    model.addAttribute("user", user);
	    return "profile";
	}
	
	@GetMapping("/edit/{id}")
	public String getUserEdit(@PathVariable Integer id, Model model) {
		User user = userService.getUserById(id);
	    model.addAttribute("user", user);
	    return "user-edit";
	}

	@PostMapping
	public String createUser(
		@RequestParam String firstname,
		@RequestParam String lastname,
		@RequestParam String password, 
		@RequestParam String phone, 
		@RequestParam String email, 
		@RequestParam String profilePicture,
		@RequestParam(defaultValue = "user") Role role,
		Model model) {
			UserCreationRequest request = new UserCreationRequest();
			request.setFirstname(firstname);
			request.setLastname(lastname);
			request.setPassword(password);
			request.setPhone(phone);
			request.setEmail(email);
			request.setProfilePicture(profilePicture);
			request.setRole(role);
			
			userService.createUser(request);
			model.addAttribute("users", userService.getUsers());
			return "redirect:login";
	}

	@PostMapping("/edit/{id}")
	public String updateUser(@PathVariable Integer id,
	                         @RequestParam String firstname,
	                         @RequestParam String lastname,
	                         @RequestParam String email,
	                         @RequestParam String phone,
	                         @RequestParam(required = false) String profilePicture,
	                         Model model) {

	    // Tạo đối tượng UserUpdateRequest và cập nhật thông tin
	    UserUpdateRequest request = new UserUpdateRequest();
	    request.setFirstname(firstname);
	    request.setLastname(lastname);
	    request.setEmail(email);
	    request.setPhone(phone);
	    request.setProfilePicture(profilePicture);

	    // Thêm thời gian cập nhật
	    LocalDateTime localDateTime = LocalDateTime.now();
	    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	    request.setUpdatedAt(date);

	    // Cập nhật thông tin người dùng
	    User updatedUser = userService.updateUser(id, request);

	    // Sau khi cập nhật, chuyển hướng tới trang chi tiết người dùng
	    return "redirect:/users/" + updatedUser.getUserId();
	}
	
	@PostMapping("/update")
	public String updateProfile(@ModelAttribute User updatedUser, HttpSession session) {
	    User currentUser = (User) session.getAttribute("currentUser");
	    if (currentUser != null) {
	        updatedUser.setUserId(currentUser.getUserId()); // giữ ID cũ
	        userService.updateUser(updatedUser);     // cập nhật thông tin
	        session.setAttribute("currentUser", updatedUser);
	    }
	    return "redirect:/profile/"+updatedUser.getUserId();
	}
	
	@PostMapping("/delete/{id}")
	public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
	    userService.deleteUser(id);
	    redirectAttributes.addFlashAttribute("successMessage", "Xóa người dùng thành công!");
	    return "redirect:/admin?section=users"; // Chuyển hướng lại trang danh sách người dùng
	}
}
