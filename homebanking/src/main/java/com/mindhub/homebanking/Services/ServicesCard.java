package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Card;

public interface ServicesCard {
    Boolean existsByNumber(String number);
    Card findByNumber(String number);
    void save(Card card);

}
