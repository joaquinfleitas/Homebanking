package com.mindhub.homebanking.Services.ServicesImpl;

import com.mindhub.homebanking.Services.ServicesCard;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServicesImpl implements ServicesCard {
    @Autowired
    private CardRepository cardRepository;
    @Override
    public Boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }
    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }
}
