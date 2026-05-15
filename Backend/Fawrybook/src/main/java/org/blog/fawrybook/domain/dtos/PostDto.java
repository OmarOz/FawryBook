package org.blog.fawrybook.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blog.fawrybook.domain.entities.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private UUID id;
    private String title;
    private String content;
    private AuthorDto author;
    private CategoryDto category;
    private Set<TagDto> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long likesCount;
    private long dislikesCount;
    private long commentsCount;
    private List<CommentDto> comments;
    private double rating;

}
