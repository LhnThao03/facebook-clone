package com.example.facebook_clone.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.FileStorageService;
import com.example.facebook_clone.service.InteractionService;
import com.example.facebook_clone.service.PostService;
import com.example.facebook_clone.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private InteractionService interactionService;
    @Autowired
    private FileStorageService fileStorageService;
    @Value("${upload.path}")
	private String uploadPath;
    
    @GetMapping
    public String getPosts(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Post> postPage = postService.getPosts(page, 10);
        model.addAttribute("postPage", postPage);
        return "admin/dashboard";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable int id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "post-detail";
    }

    @PostMapping
    public String createPost(@RequestParam Integer userId,
                             @RequestParam String content,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             Model model) {
        User user = userService.getUserById(userId);

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            String fileUrl = fileStorageService.saveFile(file, "post");

            if (file.getContentType().startsWith("image/")) {
                post.setImageUrl(fileUrl);
            } else if (file.getContentType().startsWith("video/")) {
                post.setVideoUrl(fileUrl);
            }
        }

        postService.createPost(post);

        return "redirect:/profile/" + user.getUserId();
    }

    @PostMapping("/{id}")
    public String updatePost(@PathVariable int id,
                           @RequestParam String content,
                           @RequestParam(required = false) String imageUrl,
                           @RequestParam(required = false) String videoUrl) {
        Post post = new Post();
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setVideoUrl(videoUrl);
        postService.updatePost(id, post);
        return "redirect:/posts";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable int id) {
        postService.deletePost(id);
        return "redirect:/admin?section=posts";
    }
    
    // @PostMapping("/posts/{postId}/toggle-like")
    // @ResponseBody
    // public ResponseEntity<?> toggleLike(@PathVariable Integer postId, HttpSession session) {
    //     User user = (User) session.getAttribute("currentUser");
    //     if (user == null) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //     }

    //     boolean liked = interactionService.toggleLike(postId, user.getUserId());
    //     return ResponseEntity.ok(liked ? "Liked" : "Unliked");
    // }
} 