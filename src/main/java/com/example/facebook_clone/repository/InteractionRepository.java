package com.example.facebook_clone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.facebook_clone.model.Interaction;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Integer> {
	@Query("SELECT COUNT(i) FROM Interaction i WHERE i.post.postId = :postId AND i.type = :type")
    int countInteractionsByType(@Param("postId") int postId, @Param("type") String type);
	//@Query("SELECT COUNT(i) FROM Interaction i WHERE i.post.postId = :postId AND i.type = :type")
	//int countByPostIdAndType(@Param("postId") int postId, @Param("type") com.example.facebook_clone.model.Interaction.InteractionType type);
	int countByPost_PostIdAndType(int postId, Interaction.InteractionType type);
	Optional<Interaction> findByPost_PostIdAndUser_UserIdAndType(Integer postId, Integer userId, Interaction.InteractionType type);
	List<Interaction> findByPost_PostIdAndTypeOrderByCreatedAtDesc(Integer postId, Interaction.InteractionType type);
}
