package com.app.bank.service;

import com.app.bank.dto.AccountResponse;
import com.app.bank.exception.AccountNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getAccountDetails(long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public AccountResponse getAccountDetailsWithDTO(long customerId) {
        Account account = accountRepository.findByCustomerId(customerId);

        if (account == null) {
            throw new AccountNotFoundException(customerId);
        }

        return new AccountResponse(account.getAccountNumber(), account.getCustomerId(), account.getAccountType());
    }

}
