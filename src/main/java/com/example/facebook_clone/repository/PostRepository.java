package com.example.facebook_clone.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.facebook_clone.model.Post;
import com.example.facebook_clone.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
	@Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    Page<Post> findAll(Pageable pageable);
	@Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
	List<Post> findAll();
	
	List<Post> findTop10ByOrderByCreatedAtDesc();
	
	long countByCreatedAtAfter(LocalDateTime dateTime);
	
	List<Post> findByUser_UserIdOrderByCreatedAtDesc(Integer userId);

	@Query("SELECT p FROM Post p WHERE (p.imageUrl IS NOT NULL OR p.videoUrl IS NOT NULL) AND p.user.id = :userId ORDER BY p.createdAt DESC")
	List<Post> findTop6MediaByUserId(@Param("userId") Integer userId, Pageable pageable);
}
