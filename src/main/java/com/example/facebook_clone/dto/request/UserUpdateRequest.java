package com.example.facebook_clone.dto.request;

import java.util.Date;

import com.example.facebook_clone.model.User.Gender;

import jakarta.persistence.Column;

public class UserUpdateRequest {
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    @Column(columnDefinition = "ENUM('male', 'female', 'other') DEFAULT 'other'")
    private Gender gender;
    private String profilePicture;
    private Date updatedAt = new Date();
    // Không cho phép update role qua API này để bảo mật

    public UserUpdateRequest() {}

    public UserUpdateRequest(String firstname, String lastname, String phone, String email, String profilePicture) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.profilePicture = profilePicture;
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

    public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
} 