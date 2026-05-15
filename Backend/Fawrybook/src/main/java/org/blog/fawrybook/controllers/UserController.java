package org.blog.fawrybook.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blog.fawrybook.domain.dtos.UpdateProfileRequestDto;
import org.blog.fawrybook.domain.dtos.UserProfileDto;
import org.blog.fawrybook.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<UserProfileDto> getMyProfile(
            @RequestAttribute UUID userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PutMapping
    public ResponseEntity<UserProfileDto> updateMyProfile(
            @RequestAttribute UUID userId,
            @RequestBody @Valid UpdateProfileRequestDto request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }
}
