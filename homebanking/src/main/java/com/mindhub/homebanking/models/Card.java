package com.mindhub.homebanking.models;

import com.mindhub.homebanking.repositories.CardRepository;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long Id;
    private String number;
    private String cvv;
    private CardType cardType;
    private CardColor cardColor;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private String cardHolder;
    private Boolean mostrarTarjeta;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card(){}
    public Card(String cardholder, String number, String cvv, CardType cardType, CardColor cardColor, LocalDate thruDate, LocalDate fromDate, Boolean mostrarTarjeta) {
        this.cardHolder = cardholder;
        this.number = number;
        this.cvv = cvv;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.mostrarTarjeta = mostrarTarjeta;
    }

    public Boolean getMostrarTarjeta() {
        return mostrarTarjeta;
    }

    public void setMostrarTarjeta(Boolean mostrarTarjeta) {
        this.mostrarTarjeta = mostrarTarjeta;
    }

    public long getId() {
        return Id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public String getCardholder() {
        return cardHolder;
    }

    public void setCardholder(String cardholder) {
        this.cardHolder = cardholder;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
