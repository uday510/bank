package com.app.bank.filter;

import com.app.bank.config.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthorizationHeaderFilter extends OncePerRequestFilter {

    private final List<String> openApis;

    public AuthorizationHeaderFilter(SecurityProperties securityProperties) {
        this.openApis = securityProperties.getApis();

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();

        // Check if the request is for an open API
        if (isOpenApi(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validate Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isOpenApi(String requestPath) {
        return openApis.stream().anyMatch(requestPath::startsWith);
    }
}