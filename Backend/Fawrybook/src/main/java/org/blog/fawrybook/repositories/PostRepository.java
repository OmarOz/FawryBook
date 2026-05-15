package org.blog.fawrybook.repositories;

import org.blog.fawrybook.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("SELECT p FROM Post p JOIN FETCH p.author JOIN FETCH p.category LEFT JOIN FETCH p.tags WHERE p.id = :id")
    Optional<Post> findByIdWithDetails(@Param("id") UUID id);
}
