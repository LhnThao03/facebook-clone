package com.example.facebook_clone.dto;

import java.time.LocalDateTime;
import com.example.facebook_clone.model.User;

public class CommentDTO {
    private int interactionId;
    private String content;
    private User user;
    private LocalDateTime createdAt;

    public CommentDTO() {}

    public CommentDTO(int interactionId, String content, User user, LocalDateTime createdAt) {
        this.interactionId = interactionId;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(int interactionId) {
        this.interactionId = interactionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 