package org.blog.fawrybook.services;

import org.blog.fawrybook.domain.CreatePostRequest;
import org.blog.fawrybook.domain.UpdatePostRequest;
import org.blog.fawrybook.domain.entities.Comment;
import org.blog.fawrybook.domain.entities.Post;
import org.blog.fawrybook.domain.entities.User;
import org.blog.fawrybook.enums.InteractionType;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(UUID id);
    Post interactWithPost(
            UUID postId,
            UUID userId,
            InteractionType type
    );
    Comment addComment(
            UUID postId,
            UUID userId,
            String content
    );

    Post createPost(User user, CreatePostRequest createPostRequest);

    long countLikes(UUID postId);

    long countDislikes(UUID postId);

    long countComments(UUID postId);

    double calculateRating(UUID postId);

    List<Comment> getComments(UUID id);

    void deletPost(UUID postId, UUID userId);

    Post updatePost(UUID userId, UUID postId, UpdatePostRequest updatePostRequest);
}
