package com.app.bank.controller;

import com.app.bank.dto.AccountTransactionResponse;
import com.app.bank.model.AccountTransactions;
import com.app.bank.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetail(@RequestParam long id) {
        return balanceService.getBalanceDetails(id);
    }

    @GetMapping("/balance")
    public List<AccountTransactionResponse> getBalanceDetailsWithDTO(@RequestParam long id) {
        return balanceService.getBalanceDetailsWithDTO(id);
    }

}
