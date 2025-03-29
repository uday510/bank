package com.app.bank.controller;

import com.app.bank.dto.CardResponse;
import com.app.bank.model.Card;
import com.app.bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    @GetMapping("/myCards")
    public List<Card> getCardsDetails(@RequestParam long id) {
        return cardService.getCardsDetails(id);

    }

    @GetMapping("/cards")
    public List<CardResponse> getCardDetailsWithDTO(@RequestParam long id) {
        return cardService.getCardDetails(id);
    }

}
