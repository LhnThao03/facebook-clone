package com.example.facebook_clone.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.User;

public class PostDTO {
	private int postId;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private User user;
    private LocalDateTime createdAt;
    private int likes;
    private int comments;
    private int shares;
    private List<CommentDTO> commentList;
    private boolean isLiked;
    
    public PostDTO() {}
    
	public PostDTO(int postId, String content, String imageUrl, String videoUrl, User user, LocalDateTime createdAt,
			int likes, int comments, int shares) {
		super();
		this.postId = postId;
		this.content = content;
		this.imageUrl = imageUrl;
		this.videoUrl = videoUrl;
		this.user = user;
		this.createdAt = createdAt;
		this.likes = likes;
		this.comments = comments;
		this.shares = shares;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
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
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	public List<CommentDTO> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<CommentDTO> commentList) {
		this.commentList = commentList;
	}
	public boolean getIsLiked() {
		return isLiked;
	}
	public void setIsLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}
}
