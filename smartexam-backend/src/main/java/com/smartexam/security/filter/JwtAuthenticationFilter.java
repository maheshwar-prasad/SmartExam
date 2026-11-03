package com.smartexam.security.filter;

import com.smartexam.auth.service.JwtService;
import com.smartexam.auth.service.impl.CustomUserDetailsService;
import com.smartexam.security.constant.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String path = request.getServletPath();

        return path.equals("/api/v1/auth/login")
                || path.equals("/api/v1/auth/forgot-password")
                || path.equals("/api/v1/auth/verify-otp")
                || path.equals("/api/v1/auth/reset-password");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        try {

            if (StringUtils.hasText(jwt)
                    && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                String username =
                        jwtService.extractUsername(jwt);

                if (StringUtils.hasText(username)
                        && jwtService.validateToken(jwt)) {

                    UserDetails userDetails =
                            customUserDetailsService
                                    .loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {

            SecurityContextHolder.clearContext();

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write("""
                {
                    "status": 401,
                    "error": "Unauthorized",
                    "message": "Invalid or expired JWT token"
                }
                """);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String bearer =
                request.getHeader(SecurityConstants.HEADER);

        if (StringUtils.hasText(bearer)
                && bearer.startsWith(SecurityConstants.TOKEN_PREFIX)) {

            return bearer.substring(7);
        }

        return null;
    }
}