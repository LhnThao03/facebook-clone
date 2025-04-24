package com.example.facebook_clone.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	
	public void updateBio(Integer userId, String bio) {
	    Profile profile = profileRepository.findByUser_UserId(userId);

	    profile.setBio(bio);
	    profile.setUpdatedAt(LocalDateTime.now());
	    profileRepository.save(profile);
	}
	
	public void updateLocationAndBirthdate(int userId, String location, LocalDate birthdate) {
	    Profile profile = profileRepository.findByUser_UserId(userId);
	    if (profile != null) {
	        profile.setLocation(location);
	        profile.setBirthdate(birthdate.atStartOfDay());
	        profileRepository.save(profile);
	    }
	}
	
	public void updateCoverPicture(Profile profile, String coverUrl) {
	    profile.setCoverPicture(coverUrl);
	    profileRepository.save(profile);
	}
}
