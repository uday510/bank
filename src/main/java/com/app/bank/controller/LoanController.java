package com.app.bank.controller;

import com.app.bank.dto.LoanResponse;
import com.app.bank.model.Loans;
import com.app.bank.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/myLoans")
    public List<Loans> getLoansDetails(@RequestParam long id) {
        return loanService.getLoanDetails(id);
    }

    @GetMapping("/loans")
    public List<LoanResponse> getLoanDetails(@RequestParam long id) {
        return loanService.getLoanDetailsWithDTO(id);
    }

}
