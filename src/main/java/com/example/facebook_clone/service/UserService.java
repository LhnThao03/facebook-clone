package com.example.facebook_clone.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.facebook_clone.dto.request.UserCreationRequest;
import com.example.facebook_clone.dto.request.UserUpdateRequest;
import com.example.facebook_clone.model.Friend;
import com.example.facebook_clone.model.User;
import com.example.facebook_clone.repository.FriendRepository;
import com.example.facebook_clone.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private FriendRepository friendRepository;
	
	public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
	
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	
	public List<User> getTop10Users() {
	    return userRepository.findTop10Users(PageRequest.of(0, 10));
	}

	public User getUserById(Integer id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}
	
	public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	public User createUser(UserCreationRequest request) {
		// Kiểm tra email đã tồn tại chưa
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		User user = new User();
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setPassword(request.getPassword()); // Nên mã hóa password trước khi lưu
		user.setPhone(request.getPhone());
		user.setEmail(request.getEmail());
		user.setGender(request.getGender());
		user.setProfilePicture(request.getProfilePicture());
		user.setRole(request.getRole());

		return userRepository.save(user);
	}

	public User updateUser(Integer id, UserUpdateRequest request) {
		User user = getUserById(id);

		// Kiểm tra nếu email mới đã tồn tại (trừ email hiện tại của user)
		if (!user.getEmail().equals(request.getEmail()) && 
			userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setPhone(request.getPhone());
		user.setEmail(request.getEmail());
		user.setProfilePicture(request.getProfilePicture());

		return userRepository.save(user);
	}

	public void deleteUser(Integer id) {
		if (!userRepository.existsById(id)) {
			throw new RuntimeException("User not found with id: " + id);
		}
		userRepository.deleteById(id);
	}
	
	public long getTotalUsers() {
	    return userRepository.count(); // Tổng số bản ghi
	}
	
	public List<User> getFriendSuggestions(User currentUserId) {
	    List<User> allUsers = userRepository.findAll();

	    List<Friend> allRelations = friendRepository.findAllRelations(currentUserId);

	    // Lọc tất cả người đã có mối quan hệ (pending hoặc accepted) với currentUser (dù là user1 hay user2)
	    List<User> excludedUsers = allRelations.stream()
	        .filter(friend -> 
	            friend.getStatus() == Friend.FriendshipStatus.accepted || 
	            friend.getStatus() == Friend.FriendshipStatus.pending
	        )
	        .map(friend -> 
	            friend.getUser1().equals(currentUserId) ? friend.getUser2() : friend.getUser1()
	        )
	        .collect(Collectors.toList());

	    // Thêm bản thân vào danh sách loại trừ
	    excludedUsers.add(currentUserId);

	    // Trả về người chưa có bất kỳ mối quan hệ nào với currentUser
	    return allUsers.stream()
	        .filter(user -> excludedUsers.stream()
	            .noneMatch(excluded -> excluded.getUserId() == user.getUserId()))
	        .collect(Collectors.toList());
	}
	
	public int countFriends(User user) {
        int countAsUser1 = friendRepository.countByUser1(user);
        int countAsUser2 = friendRepository.countByUser2(user);
        return countAsUser1 + countAsUser2;
    }
	
	public void updateProfilePicture(User user, String imagePath) {
	    user.setProfilePicture(imagePath);
	    userRepository.save(user);
	}
	
	public void updateUser(User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(updatedUser.getUserId());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            existingUser.setFirstname(updatedUser.getFirstname());
            existingUser.setLastname(updatedUser.getLastname());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setGender(updatedUser.getGender());

            // Nếu người dùng nhập mật khẩu mới
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword()); // ❗ Bạn nên mã hóa mật khẩu ở đây nếu có
            }

            userRepository.save(existingUser);
        }
    }
}
