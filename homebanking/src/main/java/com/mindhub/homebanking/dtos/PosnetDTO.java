package com.mindhub.homebanking.dtos;

public class PosnetDTO {

    private String number;
    private String cvv;
    private Double amount;
    private String description;

    public PosnetDTO(String number, String cvv, Double amount, String description) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
