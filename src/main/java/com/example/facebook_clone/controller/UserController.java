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
import com.example.facebook_clone.model.User.Role;

@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	
//	@GetMapping
//	public String getUsers(Model model){
//		List<User> allUsers = userService.getUsers();
//		List<User> topUsers = userService.getTop10Users();
//		model.addAttribute("allUsers", allUsers);
//		model.addAttribute("topUsers", topUsers);
//		return "admin/dashboard";
//	}
	
	//Phân trang
//	@GetMapping
//    public String getUsers(@RequestParam(defaultValue = "0") int page, Model model) {
//        Page<User> userPage = userService.getUsers(page, 10); // Lấy 10 user trên mỗi trang
//        List<User> topUsers = userService.getTop10Users();
//        model.addAttribute("userPage", userPage);
//        model.addAttribute("topUsers", topUsers);
//        return "admin/dashboard";
//    }
	
	@GetMapping("/{id}")
	public String getUserDetail(@PathVariable Integer id, Model model) {
		User user = userService.getUserById(id);
	    model.addAttribute("user", user);
	    return "profile";
	}
	
	@GetMapping("/edit/{id}")
	public String getUserDetail1(@PathVariable Integer id, Model model) {
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

	// @DeleteMapping("/delete/{id}")
	// public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
	// 	userService.deleteUser(id);
	// 	return ResponseEntity.ok().build();
	// }
	
	@PostMapping("/delete/{id}")
	public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
	    userService.deleteUser(id);
	    redirectAttributes.addFlashAttribute("successMessage", "Xóa người dùng thành công!");
	    return "redirect:/users?section=users"; // Chuyển hướng lại trang danh sách người dùng
	}
}
