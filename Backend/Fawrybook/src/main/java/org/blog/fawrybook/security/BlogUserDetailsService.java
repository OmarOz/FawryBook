package org.blog.fawrybook.security;

import lombok.RequiredArgsConstructor;
import org.blog.fawrybook.domain.entities.User;
import org.blog.fawrybook.repositories.UserRepository;
import org.blog.fawrybook.security.BlogUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException( "User name not found by email: " + email));
        return new BlogUserDetails(user);
    }
}
