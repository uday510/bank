package com.app.bank.dto;

public record LoginResponse(
        String message,
        String token
) {
}
