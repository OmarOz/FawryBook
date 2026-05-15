package org.blog.fawrybook.services;

import org.blog.fawrybook.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> tagNames);
    List<Tag> getTagByIds(Set<UUID> ids);
}
