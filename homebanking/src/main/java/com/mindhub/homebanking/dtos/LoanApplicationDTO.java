package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private Long id;
    private Double amount;
    private Integer payments;
    private String numberAccountDestiny;

    public LoanApplicationDTO(Long id, Double amount, Integer payments, String numberAccountDestiny) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountDestiny = numberAccountDestiny;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getNumberAccountDestiny() {
        return numberAccountDestiny;
    }
}
