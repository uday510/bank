package com.app.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CardsController {

    @GetMapping("/cards")
    public String getCardsDetails() {
        return "Card Details from DB";
    }

}
