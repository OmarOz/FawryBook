package org.blog.fawrybook.services.implmentation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.blog.fawrybook.domain.dtos.UpdateProfileRequestDto;
import org.blog.fawrybook.domain.dtos.UserProfileDto;
import org.blog.fawrybook.domain.entities.User;
import org.blog.fawrybook.repositories.UserRepository;
import org.blog.fawrybook.services.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "+ id));
    }

    @Override
    public UserProfileDto getProfile(UUID userId) {
        User user = getUserById(userId);
        return toProfileDto(user);
    }

    @Override
    public UserProfileDto updateProfile(UUID userId, UpdateProfileRequestDto request) {
        User user = getUserById(userId);
        user.setUsername(request.getUsername());
        userRepository.save(user);
        return toProfileDto(user);
    }

    private UserProfileDto toProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
