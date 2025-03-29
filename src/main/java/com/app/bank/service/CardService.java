package com.app.bank.service;

import com.app.bank.dto.CardResponse;
import com.app.bank.model.Card;
import com.app.bank.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public List<Card> getCardsDetails(long id) {
        return cardRepository.findByCustomerId(id);
    }

    public List<CardResponse> getCardDetails(long id) {
        return Optional.ofNullable(cardRepository.findByCustomerId(id))
                .orElse(Collections.emptyList())
                .stream()
                .map(card -> new CardResponse(
                        card.getCardId(),
                        card.getCustomerId(),
                        card.getCardNumber(),
                        card.getCardType(),
                        card.getTotalLimit(),
                        card.getAmountUsed(),
                        card.getAvailableAmount(),
                        card.getCreatedAt()
                        )

                )
                .toList();
    }
}
