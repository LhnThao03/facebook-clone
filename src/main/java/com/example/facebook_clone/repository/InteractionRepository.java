package com.example.facebook_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.facebook_clone.model.Interaction;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction , Integer> {

}
