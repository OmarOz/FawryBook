package org.blog.fawrybook.repositories;

import org.blog.fawrybook.domain.entities.PostInteraction;
import org.blog.fawrybook.enums.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostInteractionRepository extends JpaRepository<PostInteraction, UUID> {
    long countByPostIdAndType(UUID postId, InteractionType type);
    Optional<PostInteraction> findByPostIdAndUserId(
            UUID postId,
            UUID userId
    );
}
