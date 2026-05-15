package org.blog.fawrybook.mappers;

import org.blog.fawrybook.domain.dtos.CategoryDto;
import org.blog.fawrybook.domain.dtos.CreateCategoryRequestDto;
import org.blog.fawrybook.domain.entities.Category;
import org.blog.fawrybook.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto createCategoryRequestDto);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts){
        if(posts == null){return 0;}
        return posts.size();
    }
}
