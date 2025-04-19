package com.example.facebook_clone.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;
    
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
    public String createPost(@RequestParam String content,
                           @RequestParam(required = false) String imageUrl,
                           @RequestParam(required = false) String videoUrl,
                           Model model) {
        Post post = new Post();
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setVideoUrl(videoUrl);
        postService.createPost(post);
        return "redirect:/posts";
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

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable int id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
} 