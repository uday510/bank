package com.app.bank.dto;

import java.util.Date;

public record LoanResponse(
        long loanNumber,
        long customerId,
        Date startDate,
        String loanType,
        int totalLoan,
        int amountPaid,
        int outstandingAmount
) {}