package org.blog.fawrybook.controllers;

import lombok.RequiredArgsConstructor;
import org.blog.fawrybook.domain.dtos.CreateTagsRequestDto;
import org.blog.fawrybook.domain.dtos.TagDto;
import org.blog.fawrybook.domain.entities.Tag;
import org.blog.fawrybook.mappers.TagMapper;
import org.blog.fawrybook.services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags(){
        List<Tag> tags = tagService.getTags();
        List<TagDto> tagRespons = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagRespons);
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@RequestBody CreateTagsRequestDto createTagsRequestDto){
        List<Tag> savedTags = tagService.createTags((createTagsRequestDto.getNames()));
        List<TagDto> createdTagDto = savedTags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(
                createdTagDto,
                HttpStatus.CREATED
                );
    }
}
