package com.example.facebook_clone.repository;

import java.util.List;
import java.util.Optional;

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
	
	int countByUser1AndStatus(User user, Friend.FriendshipStatus status);
	int countByUser2AndStatus(User user, Friend.FriendshipStatus status);
	
	@Query("SELECT f FROM Friend f WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)")
	Optional<Friend> findByUsers(@Param("user1") User user1, @Param("user2") User user2);
	
	List<Friend> findByUser2AndStatus(User user2, Friend.FriendshipStatus status);
	
	@Query("SELECT f FROM Friend f WHERE (f.user1 = :user OR f.user2 = :user)")
	List<Friend> findAllRelations(@Param("user") User user);
	
	@Query("SELECT f FROM Friend f WHERE f.status = 'accepted' AND (f.user1 = :user OR f.user2 = :user)")
	List<Friend> findAcceptedFriends(@Param("user") User user);
}
