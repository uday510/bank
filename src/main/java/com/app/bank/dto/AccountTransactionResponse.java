package com.app.bank.dto;

import java.util.Date;

public record AccountTransactionResponse(
        String transactionId,
        long accountNumber,
        long customerId,
        Date transactionDate,
        String transactionSummary,
        String transactionType,
        int transactionAmount,
        int closingBalance,
        Date createdAt
) {}