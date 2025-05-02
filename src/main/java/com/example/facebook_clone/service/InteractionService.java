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
    public boolean toggleLike(int postId, int userId) {
        Optional<Interaction> existing = interactionRepository
            .findByPost_PostIdAndUser_UserIdAndType(postId, userId, Interaction.InteractionType.like);
        
        if (existing.isPresent()) {
            interactionRepository.delete(existing.get());
            return false;
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
            return true;
        }
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

    @Transactional
    public boolean deleteComment(int commentId, int userId) {
        Interaction comment = interactionRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bình luận!"));
        
        if (comment.getType() != Interaction.InteractionType.comment) {
            throw new IllegalArgumentException("Đây không phải là bình luận!");
        }

        if (comment.getUser().getUserId() != userId) {
            throw new SecurityException("Bạn không có quyền xóa bình luận này!");
        }

        try {
            interactionRepository.delete(comment);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi xóa bình luận!", e);
        }
    }

    public Interaction getCommentById(int commentId) {
        return interactionRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bình luận!"));
    }

    public boolean canDeleteComment(int commentId, int userId) {
        Interaction comment = getCommentById(commentId);
        return comment.getUser().getUserId() == userId;
    }

    public int getCommentCount(int postId) {
        return interactionRepository.countByPost_PostIdAndType(postId, Interaction.InteractionType.comment);
    }

    public int getLikeCount(int postId) {
        return interactionRepository.countByPost_PostIdAndType(postId, Interaction.InteractionType.like);
    }
}

