package com.app.bank.dto;

import java.sql.Date;

public record CardResponse(
        long cardId,
        long customerId,
        String cardNumber,
        String cardType,
        int totalLimit,
        int amountUsed,
        int availableAmount,
        Date createdAt
) {};