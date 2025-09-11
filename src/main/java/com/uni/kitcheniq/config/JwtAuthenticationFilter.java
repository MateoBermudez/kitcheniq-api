package com.uni.kitcheniq.config;

import com.uni.kitcheniq.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest Request, HttpServletResponse Response, FilterChain FilterChain)
            throws ServletException, IOException {

        final String Token = getTokenFromRequest(Request);
        final String UserId;

        if (Token == null){
            FilterChain.doFilter(Request, Response);
            return;
        }

        UserId = jwtService.getUserIdFromToken(Token);

        if (UserId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(UserId);

            if (jwtService.isTokenValid(Token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(Request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        FilterChain.doFilter(Request, Response);

    }

    private String getTokenFromRequest(HttpServletRequest Request) {
        final String AuthHeader = Request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(AuthHeader) && AuthHeader.startsWith("Bearer ")) {
            return AuthHeader.substring(7);
        }
        return null;
    }
}
