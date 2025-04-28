package com.example.facebook_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_clone.model.Interaction;
import com.example.facebook_clone.repository.InteractionRepository;
import com.example.facebook_clone.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class InteractionService {
	// @Autowired
    // private InteractionRepository interactionRepository;
    
    // @Autowired
    // private PostRepository postRepository;
    
    // @Transactional
    // public boolean toggleLike(Integer postId, Integer userId) {
    //     // Kiểm tra xem đã like chưa
    //     Interaction existing = interactionRepository.findByPostIdAndUserIdAndType(postId, userId, "like");

    //     Post post = postRepository.findById(postId).orElseThrow();

    //     if (existing != null) {
    //         // Đã like rồi, giờ unlike
    //         interactionRepository.delete(existing);
    //         post.setLikes(post.getLikes() - 1);
    //         postRepository.save(post);
    //         return false;
    //     } else {
    //         // Chưa like, giờ like
    //         Interaction interaction = new Interaction();
    //         interaction.setPostId(postId);
    //         interaction.setUserId(userId);
    //         interaction.setType("like");
    //         interactionRepository.save(interaction);

    //         post.setLikes(post.getLikes() + 1);
    //         postRepository.save(post);
    //         return true;
    //     }
    // }
}
