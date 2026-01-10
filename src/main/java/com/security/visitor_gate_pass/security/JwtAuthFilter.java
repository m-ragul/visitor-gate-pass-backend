package com.security.visitor_gate_pass.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = JwtUtil.extractClaims(token);

            String username = claims.getSubject();
            String rawRole = claims.get("role", String.class);

            // 🛡️ ROBUST NORMALIZATION 🛡️
            if (rawRole == null || rawRole.trim().isEmpty()) {
                rawRole = "USER"; // Default fallback
            }

            // 1. Force UpperCase
            String normalizedRole = rawRole.toUpperCase();

            // 2. Ensure "ROLE_" prefix
            if (!normalizedRole.startsWith("ROLE_")) {
                normalizedRole = "ROLE_" + normalizedRole;
            }

            // Create Authority
            var authorities = List.of(new SimpleGrantedAuthority(normalizedRole));

            var authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println(">>> CHECK AUTHENTICATION <<<");
            System.out.println("USER: " + username);
            System.out.println("RAW ROLE: " + rawRole);
            System.out.println("FINAL AUTHORITY: " + normalizedRole);

        } catch (Exception e) {
            System.err.println("JWT ERROR: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}