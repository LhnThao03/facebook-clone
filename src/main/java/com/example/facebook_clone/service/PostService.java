package com.example.facebook_clone.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.facebook_clone.dto.PostDTO;
import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.model.Interaction;
import com.example.facebook_clone.repository.InteractionRepository;
import com.example.facebook_clone.repository.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    private final String uploadDir = "facebook-clone/IMG/";
    
    @Autowired
    private InteractionRepository interactionRepository;

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
    

    public List<PostDTO> getAllPostsWithInteractions() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> postDTOs = new ArrayList<>();

        for (Post post : posts) {
            int likes = interactionRepository.countByPost_PostIdAndType(post.getPostId(), Interaction.InteractionType.like);
            int comments = interactionRepository.countByPost_PostIdAndType(post.getPostId(), Interaction.InteractionType.comment);
            int shares = interactionRepository.countByPost_PostIdAndType(post.getPostId(), Interaction.InteractionType.share);

            PostDTO dto = new PostDTO();
            dto.setPostId(post.getPostId());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            dto.setVideoUrl(post.getVideoUrl());
            dto.setUser(post.getUser());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setLikes(likes);
            dto.setComments(comments);
            dto.setShares(shares);

            postDTOs.add(dto);
        }

        return postDTOs;
    }
    
    public List<PostDTO> getPostsByUserId(Integer userId) {
        List<Post> posts = postRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
        List<PostDTO> postDTOs = new ArrayList<>();

        for (Post post : posts) {
            int likes = interactionRepository.countByPost_PostIdAndType(post.getPostId(), Interaction.InteractionType.like);
            int comments = interactionRepository.countByPost_PostIdAndType(post.getPostId(), Interaction.InteractionType.comment);
            int shares = interactionRepository.countByPost_PostIdAndType(post.getPostId(), Interaction.InteractionType.share);

            PostDTO dto = new PostDTO();
            dto.setPostId(post.getPostId());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            dto.setVideoUrl(post.getVideoUrl());
            dto.setUser(post.getUser());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setLikes(likes);
            dto.setComments(comments);
            dto.setShares(shares);

            postDTOs.add(dto);
        }

        return postDTOs;
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
    

  
    // Thư mục lưu file upload, bạn nhớ tạo thư mục này hoặc kiểm tra trước khi lưu
    public String saveFile(MultipartFile file) {
        // Nếu không có file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Tạo tên file mới để tránh trùng lặp
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        // Lấy phần đuôi file (.jpg, .png, .mp4,...)
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Tạo tên file mới random
        String filename = UUID.randomUUID().toString() + extension;

        // Tạo file mới ở thư mục uploadDir
        File destFile = new File(uploadDir + filename);

        // Nếu thư mục chưa tồn tại, tạo mới
        destFile.getParentFile().mkdirs();

        // Lưu file vào server
        try {
			file.transferTo(destFile);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Trả về đường dẫn (hoặc tên file tuỳ cách bạn lưu DB)
        return "/IMG/" + filename;  // Giả sử khi show ra sẽ lấy từ /IMG/...
    }
}
