package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Loan;
import java.util.List;

public class LoanDTO {

    private Long id;
    private String name;
    private List<Integer> payments;
    private Integer maxAmount;
    private Double fee;

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.payments = loan.getPayments();
        this.maxAmount = loan.getMaxAmount();
        this.fee= loan.getFee();
    }

    public Double getFee() {
        return fee;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public List<Integer> getPayment() {
        return payments;
    }
    public Integer getMaxAmount() {
        return maxAmount;
    }
}
