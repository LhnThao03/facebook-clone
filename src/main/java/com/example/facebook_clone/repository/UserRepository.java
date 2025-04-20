package com.example.facebook_clone.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.facebook_clone.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findTop10Users(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.userId <> :currentUserId") // Gợi ý bạn bè đơn giản
    List<User> findFriendSuggestions(@Param("currentUserId") Integer currentUserId);
    
    User findByEmail(String email); // Spring tự tạo query theo tên hàm
    
    Page<User> findAll(Pageable pageable);
}
