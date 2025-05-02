package com.example.facebook_clone.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.facebook_clone.model.Interaction;
import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.repository.InteractionRepository;
import com.example.facebook_clone.repository.PostRepository;
import com.example.facebook_clone.repository.UserRepository;

@Service
public class InteractionService {

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public int toggleLike(int postId, int userId) {
        Optional<Interaction> existing = interactionRepository.findByPost_PostIdAndUser_UserIdAndType(
            postId, userId, Interaction.InteractionType.like);
        
        if (existing.isPresent()) {
            interactionRepository.delete(existing.get());
        } else {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Interaction like = new Interaction();
            like.setPost(post);
            like.setUser(user);
            like.setType(Interaction.InteractionType.like);

            interactionRepository.save(like);
        }

        return interactionRepository.countByPost_PostIdAndType(postId, Interaction.InteractionType.like);
    }

    @Transactional
    public Interaction addComment(int postId, int userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Interaction comment = new Interaction();
        comment.setPost(post);
        comment.setUser(user);
        comment.setType(Interaction.InteractionType.comment);
        comment.setContent(content);

        return interactionRepository.save(comment);
    }

    @Transactional
    public int addShare(int postId, int userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Interaction share = new Interaction();
        share.setPost(post);
        share.setUser(user);
        share.setType(Interaction.InteractionType.share);

        interactionRepository.save(share);

        return interactionRepository.countByPost_PostIdAndType(postId, Interaction.InteractionType.share);
    }

    public int getCommentCount(int postId) {
        return interactionRepository.countByPost_PostIdAndType(postId, Interaction.InteractionType.comment);
    }
}

