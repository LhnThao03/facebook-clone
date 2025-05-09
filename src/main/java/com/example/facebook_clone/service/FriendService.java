package com.example.facebook_clone.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_clone.model.Friend;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.repository.FriendRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FriendService {
	@Autowired
	private FriendRepository friendRepository;
	public void sendFriendRequest(User sender, User receiver) {
	    // Kiểm tra đã có mối quan hệ nào chưa
	    Optional<Friend> existing = friendRepository.findByUsers(sender, receiver);
	    if (existing.isPresent()) {
	        throw new IllegalStateException("Đã tồn tại lời mời hoặc là bạn bè");
	    }

	    Friend friend = new Friend();
	    friend.setUser1(sender);
	    friend.setUser2(receiver);
	    friend.setStatus(Friend.FriendshipStatus.pending);
	    friend.setCreatedAt(LocalDateTime.now());

	    friendRepository.save(friend);
	}
	
	public void acceptFriendRequest(User receiver, User sender) {
	    Friend friend = friendRepository.findByUsers(sender, receiver)
	        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lời mời"));

	    if (friend.getStatus() != Friend.FriendshipStatus.pending) {
	        throw new IllegalStateException("Không thể chấp nhận lời mời không hợp lệ");
	    }

	    friend.setStatus(Friend.FriendshipStatus.accepted);
	    friendRepository.save(friend);
	}
	
	public List<Friend> getPendingRequests(User receiver) {
	    return friendRepository.findByUser2AndStatus(receiver, Friend.FriendshipStatus.pending);
	}
	
	public void acceptRequest(int friendshipId) {
	    Friend friend = friendRepository.findById(friendshipId).orElseThrow();
	    friend.setStatus(Friend.FriendshipStatus.accepted);
	    friendRepository.save(friend);
	}

	public void declineRequest(int friendshipId) {
	    Friend friend = friendRepository.findById(friendshipId).orElseThrow();
	    friendRepository.delete(friend);
	}
	
	public List<Friend> getAcceptedFriends(User currentUser) {
	    return friendRepository.findAcceptedFriends(currentUser);
	}
	
	public void removeFriend(User currentUser, int friendshipId) {
	    // Tìm mối quan hệ kết bạn theo friendshipId
	    Friend friend = friendRepository.findById(friendshipId)
	        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy kết bạn"));
	    
	        // Xóa kết bạn
	        friendRepository.delete(friend);
	}
	
	public Optional<Friend> getFriendRelation(User user1, User user2) {
	    return friendRepository.findByUsers(user1, user2);
	}
}
