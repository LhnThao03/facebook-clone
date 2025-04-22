package com.example.facebook_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.facebook_clone.model.Friend;
import com.example.facebook_clone.model.User;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
	int countByUser1(User user);
	
	int countByUser2(User user);
}
