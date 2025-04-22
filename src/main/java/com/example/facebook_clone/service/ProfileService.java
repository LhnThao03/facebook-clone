package com.example.facebook_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_clone.model.Profile;
import com.example.facebook_clone.repository.ProfileRepository;

@Service
public class ProfileService {
	@Autowired 
	private ProfileRepository profileRepository;
	
	public Profile getProfileByUserId(Integer userId) {
        return profileRepository.findByUser_UserId(userId);
    }
}
