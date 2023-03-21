package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public class CardDTO {
    private long Id;
    private String number;
    private String cvv;
    private CardType cardType;
    private CardColor cardColor;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private String cardholder;
    public CardDTO(Card card) {
        this.cardholder = card.getCardholder();
        this.Id = card.getId();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
    }
    public long getId() {
        return Id;
    }

    public String getCardholder() {
        return cardholder;
    }
    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

}
