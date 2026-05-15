package org.blog.fawrybook.domain;

import jakarta.validation.constraints.NotBlank;
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
public class CreatePostRequest {

    private String title;
    private String content;
    private UUID categoryId;
    private Set<UUID> tagIds = new HashSet<>();
}
