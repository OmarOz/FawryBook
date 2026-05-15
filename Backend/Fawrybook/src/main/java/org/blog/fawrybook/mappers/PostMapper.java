package org.blog.fawrybook.mappers;

import org.blog.fawrybook.domain.CreatePostRequest;
import org.blog.fawrybook.domain.UpdatePostRequest;
import org.blog.fawrybook.domain.dtos.CreatePostRequestDto;
import org.blog.fawrybook.domain.dtos.PostDto;
import org.blog.fawrybook.domain.dtos.UpdatePostRequestDto;
import org.blog.fawrybook.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "author.username", source = "author.username")
    PostDto toDto(Post post);

    @Mapping(target = "categoryId", source = "categoryId")
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);
}
