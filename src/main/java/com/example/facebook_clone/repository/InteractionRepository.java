package com.example.facebook_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.facebook_clone.model.Interaction;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction , Integer> {
	@Query("SELECT COUNT(i) FROM Interaction i WHERE i.post.postId = :postId AND i.type = :type")
    int countInteractionsByType(@Param("postId") int postId, @Param("type") String type);
	
	int countByPost_PostIdAndType(int postId, Interaction.InteractionType type);
}
