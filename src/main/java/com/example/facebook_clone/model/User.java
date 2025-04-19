package com.example.facebook_clone.model;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(unique = true, nullable = false)
    private String firstname;

    @Column(unique = true, nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('male', 'female', 'other') DEFAULT 'other'")
    private Gender gender;
    
    @Column
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('user', 'admin') DEFAULT 'user'")
    private Role role = Role.user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
    
    public enum Role {
        user, admin
    }
    
    public enum Gender{
    	male, female, other
    }

    public User() {}

    public User(int userId, String firstname, String lastname, String password, String phone, String email,
            String profilePicture, Role role, Date createdAt, Date updatedAt) {
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
