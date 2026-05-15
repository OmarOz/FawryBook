package org.blog.fawrybook.services;

import org.blog.fawrybook.domain.dtos.UpdateProfileRequestDto;
import org.blog.fawrybook.domain.dtos.UserProfileDto;
import org.blog.fawrybook.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
    UserProfileDto getProfile(UUID userId);
    UserProfileDto updateProfile(UUID userId, UpdateProfileRequestDto request);
}
