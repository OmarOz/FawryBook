package org.blog.fawrybook.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blog.fawrybook.enums.InteractionType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractDto {
    private UUID postId;
    private InteractionType interactionType;
}
