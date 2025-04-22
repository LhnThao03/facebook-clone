package com.example.facebook_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.facebook_clone.dto.request.UserCreationRequest;
import com.example.facebook_clone.dto.request.UserUpdateRequest;
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
	
	public User findByEmail(String email) {
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
        return userRepository.findFriendSuggestions(currentUserId.getUserId());
    }
	
	public int countFriends(User user) {
        int countAsUser1 = friendRepository.countByUser1(user);
        int countAsUser2 = friendRepository.countByUser2(user);
        return countAsUser1 + countAsUser2;
    }
}
