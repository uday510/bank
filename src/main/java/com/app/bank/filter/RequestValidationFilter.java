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

    private static final String BASIC_AUTH_PREFIX = "Basic ";
    private static final String INVALID_AUTH_MSG = "Invalid authentication token.";
    private static final String DECODE_FAILURE_MSG = "Failed to decode authentication token.";
    private static final String TEST_USER_DENIED_MSG = "Test users are not allowed.";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (!isValidBasicAuthHeader(authorizationHeader)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String credentials = extractCredentials(authorizationHeader);
            String email = extractEmail(credentials);

            if (isTestUser(email)) {
                sendErrorResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, TEST_USER_DENIED_MSG);
                return;
            }

        } catch (IllegalArgumentException e) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, DECODE_FAILURE_MSG);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isValidBasicAuthHeader(String header) {
        return StringUtils.hasText(header) && header.trim().startsWith(BASIC_AUTH_PREFIX);
    }

    private String extractCredentials(String header) {
        String base64Encoded = header.substring(BASIC_AUTH_PREFIX.length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Encoded);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    private String extractEmail(String credentials) {
        int delimiterIndex = credentials.indexOf(":");
        if (delimiterIndex == -1) {
            throw new IllegalArgumentException(INVALID_AUTH_MSG);
        }
        return credentials.substring(0, delimiterIndex).toLowerCase();
    }

    private boolean isTestUser(String email) {
        return email.contains("test");
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }
}