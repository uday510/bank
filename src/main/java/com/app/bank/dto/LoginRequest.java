package com.app.bank.dto;

public record LoginRequest(
        String email,
        String password
) {
}
