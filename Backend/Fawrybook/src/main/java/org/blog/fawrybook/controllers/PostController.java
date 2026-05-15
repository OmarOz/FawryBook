package org.blog.fawrybook.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.fawrybook.domain.CreatePostRequest;
import org.blog.fawrybook.domain.UpdatePostRequest;
import org.blog.fawrybook.domain.dtos.*;
import org.blog.fawrybook.domain.entities.Comment;
import org.blog.fawrybook.domain.entities.Post;
import org.blog.fawrybook.domain.entities.User;
import org.blog.fawrybook.mappers.PostMapper;
import org.blog.fawrybook.services.PostService;
import org.blog.fawrybook.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostMapper postMapper;

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();

        List<PostDto> postDtos = posts.stream().map(post -> {

            PostDto dto = postMapper.toDto(post);

            long likes = postService.countLikes(post.getId());

            long dislikes = postService.countDislikes(post.getId());

            long commentsCount = postService.countComments(post.getId());

            List<Comment> comments = postService.getComments(post.getId());
            List<CommentDto> commentDtos = comments.stream()
                    .map(comment -> CommentDto.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .authorUsername(comment.getAuthor().getUsername())
                            .createdAt(comment.getCreatedAt())
                            .build())
                    .toList();

            double rating = postService.calculateRating(post.getId());

            dto.setLikesCount(likes);
            dto.setDislikesCount(dislikes);
            dto.setCommentsCount(commentsCount);
            dto.setRating(rating);
            dto.setComments(commentDtos);

            return dto;

        }).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping("/interaction")
    public ResponseEntity<PostDto> interactWithPost(@RequestBody InteractDto interactDto,
                                                    @RequestAttribute UUID userId){

        Post post = postService.interactWithPost(
                interactDto.getPostId()
                ,userId
                ,interactDto.getInteractionType());

        return ResponseEntity.ok(
                postMapper.toDto(post)
        );
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentDto> addComment(
            @RequestBody CommentDto commentDto,
            @RequestAttribute UUID userId
    ) {
        User user= userService.getUserById(userId);

        Comment comment = postService.addComment(
                commentDto.getPostId(),
                userId,
                commentDto.getContent()
        );

        CommentDto response = CommentDto.builder()
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId){
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost = postService.createPost(loggedInUser,createPostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);

        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable UUID id) {
        Post post = postService.getPostById(id);
        PostDto dto = postMapper.toDto(post);

        dto.setLikesCount(postService.countLikes(id));
        dto.setDislikesCount(postService.countDislikes(id));
        dto.setCommentsCount(postService.countComments(id));
        dto.setRating(postService.calculateRating(id));

        List<Comment> comments = postService.getComments(id);
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> CommentDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .authorUsername(comment.getAuthor().getUsername())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();
        dto.setComments(commentDtos);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID id,
            @RequestAttribute UUID userId){
        postService.deletPost(id,userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @RequestAttribute UUID userId,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto
            ){
        Set<UUID> updatePostRequestTagIds = updatePostRequestDto.getTagIds();
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(userId,id,updatePostRequest);
        PostDto postDto = postMapper.toDto(updatedPost);
        return ResponseEntity.ok(postDto);
    }


}
