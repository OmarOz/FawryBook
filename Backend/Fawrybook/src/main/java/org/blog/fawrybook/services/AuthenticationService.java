package org.blog.fawrybook.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    UserDetails authenticate(String email,String password);
    UserDetails register(String email,String username,String password);
    String generateToken(UserDetails userDetails);

    UserDetails ValidateToken(String token);

}
