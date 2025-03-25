package com.app.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BalanceController {

    @GetMapping("/alance")
    public String getBalanceDetail() {
        return "Balance Information from DB";
    }
}
