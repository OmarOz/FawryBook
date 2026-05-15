package org.blog.fawrybook.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.blog.fawrybook.component.TokenBlacklist;
import org.blog.fawrybook.domain.dtos.AuthResponseDto;
import org.blog.fawrybook.domain.dtos.LoginRequestDto;
import org.blog.fawrybook.domain.dtos.RegisterRequestDto;
import org.blog.fawrybook.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final TokenBlacklist tokenBlacklist;
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        UserDetails userDetails = authenticationService.authenticate(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );
        String tokenValue = authenticationService.generateToken(userDetails);
        AuthResponseDto authResponseDto = AuthResponseDto.builder().token(tokenValue).expiresIn(86400).build();
        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerRequest){
        UserDetails userDetails = authenticationService.register(
                registerRequest.getEmail(),
                registerRequest.getUsername(),
                registerRequest.getPassword()
        );
        String tokenValue = authenticationService.generateToken(userDetails);
        AuthResponseDto authResponseDto = AuthResponseDto.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();
        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping("/logout")
    ResponseEntity<String> logout(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            String token = bearerToken.substring(7);
            tokenBlacklist.blacklist(token);
        }
        else{throw new IllegalArgumentException("No token provided");}
        return ResponseEntity.ok("Logged out successfully");
    }

}
