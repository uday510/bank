package com.app.bank.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !StringUtils.startsWithIgnoreCase(header.trim(), "Basic ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            byte[] decoded = Base64.getDecoder().decode(header.substring(6).trim().getBytes(StandardCharsets.UTF_8));
            String token = new String(decoded, StandardCharsets.UTF_8);
            int delimiter = token.indexOf(":");

            if (delimiter == -1) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication token.");
                return;
            }

            String email = token.substring(0, delimiter).toLowerCase();

            if (email.contains("test")) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Test users are not allowed.");
                return;
            }

        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to decode authentication token.");
            return;
        }

        chain.doFilter(request, response);
    }
}