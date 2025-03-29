package com.app.bank.service;

import com.app.bank.dto.AccountTransactionResponse;
import com.app.bank.model.AccountTransactions;
import com.app.bank.repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final AccountTransactionRepository accountTransactionRepository;

    public List<AccountTransactions> getBalanceDetails(Long customerId) {
        return accountTransactionRepository.findByCustomerIdOrderByTransactionDateDesc(customerId);
    }

    public List<AccountTransactionResponse> getBalanceDetailsWithDTO(long customerId) {
        return accountTransactionRepository.findByCustomerIdOrderByTransactionDateDesc(customerId)
                .stream()
                .map(tx -> new AccountTransactionResponse(
                        tx.getTransactionId(),
                        tx.getAccountNumber(),
                        tx.getCustomerId(),
                        tx.getTransactionDate(),
                        tx.getTransactionSummary(),
                        tx.getTransactionType(),
                        tx.getTransactionAmount(),
                        tx.getClosingBalance(),
                        tx.getCreatedAt()

                )).toList();
    }
}
