package com.example.facebook_clone.dto.request;

import java.util.Date;
import jakarta.persistence.Column;
import com.example.facebook_clone.model.User.Role;

public class UserCreationRequest {
	private String firstname;
	private String lastname;
	private String password;
	private String phone;
	private String email;
	private String profilePicture;
	
	@Column(columnDefinition = "ENUM('user', 'admin') DEFAULT 'user'")
	private Role role = Role.user;
	private Date createdAt = new Date();
	private Date updatedAt = new Date();

	public UserCreationRequest() {}

	public UserCreationRequest(String firstname, String lastname, String password, String phone, String email,
			String profilePicture, Role role, Date createdAt, Date updatedAt) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.profilePicture = profilePicture;
		this.role = role;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
