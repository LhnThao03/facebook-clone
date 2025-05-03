package com.example.facebook_clone.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import com.example.facebook_clone.model.Friend;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.FriendService;
import com.example.facebook_clone.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/friends")
public class FriendController {
	@Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @PostMapping("/send-friend-request/{receiverId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable int receiverId, HttpSession session) {
        User sender = (User) session.getAttribute("currentUser"); // LẤY TỪ SESSION

        if (sender == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập trước");
        }

        User receiver = userService.getUserById(receiverId);
        friendService.sendFriendRequest(sender, receiver);
        return ResponseEntity.ok("Request sent");
    }
    
    @GetMapping("/friend-requests")
    public String viewFriendRequests(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login"; // hoặc trang lỗi nếu chưa đăng nhập
        }

        List<Friend> pendingRequests = friendService.getPendingRequests(currentUser);
        model.addAttribute("requests", pendingRequests);
        return "friend"; // Tên file .html
    }
    
    @PostMapping("/accept-friend-request/{id}")
    public String acceptRequest(@PathVariable int id) {
        friendService.acceptRequest(id);
        return "redirect:/friend";
    }

    @PostMapping("/decline-friend-request/{id}")
    public String declineRequest(@PathVariable int id) {
        friendService.declineRequest(id);
        return "redirect:/friend";
    }
}
