package com.app.bank.controller;

import com.app.bank.dto.AccountResponse;
import com.app.bank.model.Account;
import com.app.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/myAccount")
    public Account getAccountDetails(@RequestParam long id) {
        return accountService.getAccountDetails(id);
    }

    @GetMapping("/accounts")
    public AccountResponse getAccountDetailsWithDTO(@RequestParam long id) {
        return accountService.getAccountDetailsWithDTO(id);
    }
}
