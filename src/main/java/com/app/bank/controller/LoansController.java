package com.app.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoansController {

    @GetMapping("/loans")
    public String getLoansDetails() {
        return "Loan details from DB";
    }

}
