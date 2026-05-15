package org.blog.fawrybook.repositories;

import org.blog.fawrybook.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    // get all comments for a specific post
    List<Comment> findByPostId(UUID postId);
    long countByPostId(UUID postId);
    List <Comment> findAllByPostId(UUID postId);
}
