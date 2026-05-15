package org.blog.fawrybook.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private UUID id;
    private UUID postId;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
}
