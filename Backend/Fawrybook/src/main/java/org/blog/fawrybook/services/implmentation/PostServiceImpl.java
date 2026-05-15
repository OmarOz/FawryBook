package org.blog.fawrybook.services.implmentation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.fawrybook.domain.CreatePostRequest;
import org.blog.fawrybook.domain.UpdatePostRequest;
import org.blog.fawrybook.domain.entities.*;
import org.blog.fawrybook.enums.InteractionType;
import org.blog.fawrybook.repositories.CommentRepository;
import org.blog.fawrybook.repositories.PostInteractionRepository;
import org.blog.fawrybook.repositories.PostRepository;
import org.blog.fawrybook.repositories.UserRepository;
import org.blog.fawrybook.services.CategoryService;
import org.blog.fawrybook.services.PostService;
import org.blog.fawrybook.services.TagService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostInteractionRepository postInteractionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(UUID id) {
        return postRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
    }

    @Override
    public Post interactWithPost(UUID postId, UUID userId, InteractionType type) {
        // 1. get post
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        // 2. get user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // 3. check existing interaction
        Optional<PostInteraction> existingInteraction =
                postInteractionRepository
                        .findByPostIdAndUserId(postId, userId);

        // 4. update OR create
        if(existingInteraction.isPresent()) {

            PostInteraction interaction =
                    existingInteraction.get();

            interaction.setType(type);

            postInteractionRepository.save(interaction);

        } else {

            PostInteraction interaction =
                    PostInteraction.builder()
                            .post(post)
                            .user(user)
                            .type(type)
                            .build();

            postInteractionRepository.save(interaction);
        }

        return post;
    }

    @Override
    public Comment addComment(UUID postId, UUID userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Comment comment = Comment.builder()
                .content(content)
                .post(post)
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        log.info(user.getEmail());
        newPost.setAuthor(user);

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    public long countLikes(UUID postId) {
        return postInteractionRepository
                .countByPostIdAndType(
                        postId,
                        InteractionType.LIKE
                );
    }

    @Override
    public long countDislikes(UUID postId) {
        return postInteractionRepository
                .countByPostIdAndType(
                        postId,
                        InteractionType.DISLIKE
                );
    }

    @Override
    public long countComments(UUID postId) {
        return commentRepository.countByPostId(postId);
    }

    @Override
    public double calculateRating(UUID postId) {

        long likes = countLikes(postId);

        long dislikes = countDislikes(postId);

        long total = likes + dislikes;

        if(total == 0) {
            return 0;
        }

        return ((double) likes / total);
    }

    @Override
    public List<Comment> getComments(UUID id) {
        return commentRepository.findAllByPostId(id);
    }

    @Override
    public void deletPost(UUID postId, UUID userId) {
        Post existingPost = getPostById(postId);
        log.info(userId + "\n" + existingPost.getAuthor().getId());
        if(!existingPost.getAuthor().getId().equals(userId)){
            throw new BadCredentialsException("Invalid user, You are not the Author of this post");
        }
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public Post updatePost(UUID userId, UUID postId, UpdatePostRequest updatePostRequest) {
        Post existingPost = getPostById(postId);
        log.info(userId + "\n" + existingPost.getAuthor().getId());
        if(!existingPost.getAuthor().getId().equals(userId)){
            throw new BadCredentialsException("Invalid user, You are not the Author of this post");
        }
        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
            existingPost.setCategory(categoryService.getCategoryById(updatePostRequestCategoryId));
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds=updatePostRequest.getTagIds();
        if(!existingTagIds.equals(updatePostRequestTagIds)){
            List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.getTags().clear();
            existingPost.getTags().addAll(newTags);
        }
        return postRepository.save(existingPost);

    }
}
