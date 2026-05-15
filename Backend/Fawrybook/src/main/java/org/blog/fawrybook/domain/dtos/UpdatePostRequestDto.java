package org.blog.fawrybook.domain.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequestDto {
    @NotNull(message = "Post ID is required")
    private UUID id;

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "content is required")
    private String content;

    @NotNull(message = "category ID is required")
    private UUID categoryId;

    @Builder.Default
    private Set<UUID> tagIds = new HashSet<>();
}
