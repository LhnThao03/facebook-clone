package com.example.facebook_clone.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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

import com.example.facebook_clone.model.Interaction;
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
    
    @PostMapping("/{postId}/like")
    @ResponseBody
    public ResponseEntity<?> toggleLike(@PathVariable Integer postId, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            boolean isLiked = interactionService.toggleLike(postId, currentUser.getUserId());
            int likeCount = interactionService.getLikeCount(postId);

            Map<String, Object> response = new HashMap<>();
            response.put("likeCount", likeCount);
            response.put("isLiked", isLiked);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{postId}/comment")
    @ResponseBody
    public ResponseEntity<?> addComment(@PathVariable Integer postId, 
                                      @RequestParam String content,
                                      HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Interaction comment = interactionService.addComment(postId, currentUser.getUserId(), content);
            
            Map<String, Object> response = new HashMap<>();
            response.put("interactionId", comment.getInteractionId());
            response.put("content", comment.getContent());
            response.put("user", Map.of(
                "firstname", comment.getUser().getFirstname(),
                "lastname", comment.getUser().getLastname(),
                "profilePicture", comment.getUser().getProfilePicture()
            ));
            response.put("createdAt", comment.getCreatedAt());
            response.put("totalComments", interactionService.getCommentCount(postId));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{postId}/share")
    @ResponseBody
    public ResponseEntity<?> sharePost(@PathVariable Integer postId, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Post sharedPost = postService.sharePost(postId, currentUser.getUserId());
            int shareCount = interactionService.addShare(postId, currentUser.getUserId());

            return ResponseEntity.ok(Map.of(
                "shareCount", shareCount,
                "sharedPostId", sharedPost.getPostId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Vui lòng đăng nhập để thực hiện chức năng này!"));
            }

            // Lấy thông tin comment trước khi xóa để biết postId
            Interaction comment = interactionService.getCommentById(commentId);
            int postId = comment.getPost().getPostId();

            // Xóa comment
            boolean deleted = interactionService.deleteComment(commentId, currentUser.getUserId());
            
            if (deleted) {
                // Lấy số lượng comment mới
                int newCommentCount = interactionService.getCommentCount(postId);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Đã xóa bình luận thành công!");
                response.put("totalComments", newCommentCount);
                response.put("postId", postId);
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Không thể xóa bình luận!"));
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Có lỗi xảy ra khi xóa bình luận!"));
        }
    }
} 