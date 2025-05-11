package com.example.facebook_clone.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import com.example.facebook_clone.model.Friend;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.FriendService;
import com.example.facebook_clone.service.UserService;

import jakarta.persistence.EntityNotFoundException;
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
    public String viewFriendRequests(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        List<Friend> pendingRequests = friendService.getPendingRequests(currentUser);
        List<Friend> acceptedFriends = friendService.getAcceptedFriends(currentUser);

        model.addAttribute("requests", pendingRequests);
        model.addAttribute("friends", acceptedFriends);
        model.addAttribute("currentUser", currentUser);
        return "friend";
    }
    
    @PostMapping("/accept-friend-request/{id}")
    public String acceptRequest(@PathVariable int id) {
        friendService.acceptRequest(id);
        return "redirect:/friends/friend-requests";
    }

    @PostMapping("/accept-friend-request-ajax/{id}")
    @ResponseBody
    public ResponseEntity<?> acceptRequestAJAX(@PathVariable int id) {
        try {
            friendService.acceptRequest(id);
            return ResponseEntity.ok().build(); // Trả về 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

    @PostMapping("/decline-friend-request/{id}")
    public String declineRequest(@PathVariable int id) {
        friendService.declineRequest(id);
        return "redirect:/friends/friend-requests";
    }
    
    @PostMapping("/decline-friend-request-ajax/{id}")
    @ResponseBody
    public ResponseEntity<?> declineRequestAjax(@PathVariable int id) {
        try {
            friendService.declineRequest(id);
            return ResponseEntity.ok().build(); // Trả về 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }
    
    @GetMapping("/remove-friend/{id}")
    public String removeFriend(@PathVariable("id") int friendshipId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        friendService.removeFriend(currentUser, friendshipId);
        return "redirect:/friends/friend-requests";
    }
    
    @GetMapping("/remove-friend-profile/{id}")
    public String removeFriendProfile(@PathVariable("id") int friendshipId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        friendService.removeFriend(currentUser, friendshipId);
        return "redirect:/profile/" + currentUser.getUserId() + "?tab=friends";
    }
    @GetMapping("/remove-friend-profile-view/{id}")
    public String removeFriendProfileView(@PathVariable("id") int friendshipId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        friendService.removeFriend(currentUser, friendshipId);
        return "redirect:/profile/" + currentUser.getUserId() + "?tab=friends";
    }

}
