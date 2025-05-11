package com.example.facebook_clone.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.multipart.MultipartFile;

import com.example.facebook_clone.dto.PostDTO;
import com.example.facebook_clone.model.Friend;
import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.Profile;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.FileStorageService;
import com.example.facebook_clone.service.FriendService;
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
	@Autowired
	private FriendService friendService;
	@Value("${upload.path}")
	private String uploadPath;
	@Autowired
	private FileStorageService fileStorageService;
	
	// @GetMapping("{id}")
    // public String getUserProfile(@PathVariable Integer id, Model model) {
    //     User user = userService.getUserById(id);
    //     Profile profile = profileService.getProfileByUserId(id);
    //     List<PostDTO> posts = postService.getPostsByUserId(id);
    //     int friendCount = userService.countFriends(user);
    //     List<Post> mediaPosts = postService.getLatestMediaPostsByUserId(id, 9);
        
    //     model.addAttribute("mediaPosts", mediaPosts);
    //     model.addAttribute("user", user);
    //     model.addAttribute("profile",profile);
    //     model.addAttribute("posts", posts);
    //     model.addAttribute("friendCount", friendCount);

    //     return "profile"; // trỏ tới file profile.html
    // }
	
	@GetMapping("{id}")
	public String viewProfile(@PathVariable int id,
	                          @RequestParam(defaultValue = "posts") String tab,
	                          Model model) {
	    User user = userService.getUserById(id);
	    Profile profile = profileService.getProfileByUserId(id);
	    List<Friend> friends = friendService.getAcceptedFriends(user);
	    List<PostDTO> posts = postService.getPostsByUserId(id);
        int friendCount = userService.countFriends(user);
        List<Post> mediaPosts = postService.getLatestMediaPostsByUserId(id, 9);
        
        model.addAttribute("mediaPosts", mediaPosts);
        model.addAttribute("user", user);
        model.addAttribute("profile",profile);
        model.addAttribute("friendCount", friendCount);
	    model.addAttribute("activeTab", tab);

	    if (tab.equals("posts")) {
	        model.addAttribute("posts", posts);
	    } else if (tab.equals("friends")) {
	        model.addAttribute("friends", friends);
	    }

	    return "profile"; // Tên template
	}
	
	@GetMapping("/view/{id}")
	public String getUserProfileView(@PathVariable Integer id,@RequestParam(defaultValue = "posts") String tab, Model model, HttpSession session) {
	    User user = userService.getUserById(id);
	    Profile profile = profileService.getProfileByUserId(id);
		List<Friend> friends = friendService.getAcceptedFriends(user);
	    List<PostDTO> posts = postService.getPostsByUserId(id);
	    int friendCount = userService.countFriends(user);
	    List<Post> mediaPosts = postService.getLatestMediaPostsByUserId(id, 9);
	    User currentUser = (User) session.getAttribute("currentUser");
	    Optional<Friend> relationOpt = friendService.getFriendRelation(currentUser, user);
	    String friendshipStatus = "none";
	    boolean isRequestReceiver = false;
	    Integer friendshipId = null;
	    
	    
	    if (tab.equals("posts")) {
	        model.addAttribute("posts", posts);
	    } else if (tab.equals("friends")) {
	        model.addAttribute("friends", friends);
	    }
	    
	    if (relationOpt.isPresent()) {
	        Friend relation = relationOpt.get();
	        friendshipId = relation.getFriendshipId(); // Lấy ID để dùng trong view

	        if (relation.getStatus() == Friend.FriendshipStatus.accepted) {
	            friendshipStatus = "friends";
	        } else if (relation.getStatus() == Friend.FriendshipStatus.pending) {
	            if (relation.getUser1().equals(currentUser)) {
	                friendshipStatus = "pending"; // Đã gửi
	            } else {
	                isRequestReceiver = true; // Là người nhận
	                friendshipStatus = "pending";
	            }
	        }
	    }
		
	    model.addAttribute("friendshipStatus", friendshipStatus);
	    model.addAttribute("isRequestReceiver", isRequestReceiver);
	    model.addAttribute("friendshipId", friendshipId);
	    model.addAttribute("activeTab", tab);
	    model.addAttribute("currentUser", currentUser);
	    model.addAttribute("mediaPosts", mediaPosts);
	    model.addAttribute("user", user);
	    model.addAttribute("profile", profile);
	    model.addAttribute("posts", posts);
	    model.addAttribute("friendCount", friendCount);

	    return "profile-view";
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
	
	@PostMapping("/upload-cover")
	public String uploadCover(@RequestParam("coverImage") MultipartFile file, HttpSession session) throws IOException {
	    // Lấy user từ session
	    User user = (User) session.getAttribute("currentUser");

	    String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
	    Path uploadPath = Paths.get("IMG");
	    Files.createDirectories(uploadPath);

	    Path filePath = uploadPath.resolve(filename);
	    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	    // Cập nhật ảnh bìa vào DB
	    profileService.updateCoverPicture(user.getProfile(), "/IMG/" + filename);

	    return "redirect:/profile/" + user.getUserId();
	}
	
	@PostMapping("/upload-avatar")
	public String uploadAvatar(@RequestParam("avatarImage") MultipartFile file, HttpSession session) {
	    User user = (User) session.getAttribute("currentUser");
	    if (user == null) {
	        return "redirect:/login";
	    }

	    String imageUrl = fileStorageService.saveFile(file, "avatar");
	    userService.updateProfilePicture(user, imageUrl);

	    return "redirect:/profile/" + user.getUserId();
	}
}
