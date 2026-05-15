package org.blog.fawrybook.services.implmentation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.blog.fawrybook.domain.entities.Tag;
import org.blog.fawrybook.repositories.TagRepository;
import org.blog.fawrybook.services.TagService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTag = tagRepository.findByNameIn(tagNames);

        Set<String> existingTagNames = existingTag.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        List<Tag> savedTags = new ArrayList<>();
        if(!newTags.isEmpty()){
            savedTags = tagRepository.saveAll(newTags);
        }
        savedTags.addAll(existingTag);
        return savedTags;
    }

    @Override
    public List<Tag> getTagByIds(Set<UUID> ids) {
        List<Tag> foundTags = tagRepository.findAllById(ids);
        if(foundTags.size() != ids.size()){
            throw new EntityNotFoundException("Not All specified Tags Ids exist");
        }
        return foundTags;
    }
}
