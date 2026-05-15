package org.blog.fawrybook.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.fawrybook.component.TokenBlacklist;
import org.blog.fawrybook.services.AuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final TokenBlacklist tokenBlacklist;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractToken(request);

            if (tokenBlacklist.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            if (token != null) {
                UserDetails userDetails = authenticationService.ValidateToken(token);

                UsernamePasswordAuthenticationToken authetication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authetication);

                if (userDetails instanceof BlogUserDetails) {
                    request.setAttribute("userId", ((BlogUserDetails) userDetails).getId());
                }
            }
        }catch (Exception ex){
            log.warn("Invalid token");
        }

        filterChain.doFilter(request,response);
    }

    private String extractToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
