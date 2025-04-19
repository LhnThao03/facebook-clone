package com.example.facebook_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.facebook_clone.model.ActivityLog;
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {

}
