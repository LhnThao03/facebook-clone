package com.example.facebook_clone.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.repository.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Page<Post> getPosts(int page, int size) {
    	Pageable pageable = PageRequest.of(page, size);
    	return postRepository.findAll(pageable);
    }
    
    public List<Post> getPosts(){
    	return postRepository.findAll();
    }

    public Post getPostById(int id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }
    
    public List<Post> getPostsByUserId(Integer userId) {
        return postRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(int id, Post postDetails) {
        Post post = getPostById(id);
        post.setContent(postDetails.getContent());
        post.setImageUrl(postDetails.getImageUrl());
        post.setVideoUrl(postDetails.getVideoUrl());
        return postRepository.save(post);
    }

    public void deletePost(int id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
    
    public long countPostsToday() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        return postRepository.countByCreatedAtAfter(todayStart);
    }
    
    public List<Post> getLatestPosts() {
        return postRepository.findTop10ByOrderByCreatedAtDesc();
    }
    
}
