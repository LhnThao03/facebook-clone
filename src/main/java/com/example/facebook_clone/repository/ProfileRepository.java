package com.example.facebook_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.facebook_clone.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
	Profile findByUser_UserId(Integer userId);
}
