package com.app.bank.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long customerId) {
        super("Account not found for customer ID: " + customerId);
    }
}