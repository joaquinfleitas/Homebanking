package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private Double fee;
    private String name;
    private Integer maxAmount;

    @ElementCollection
    @Column (name = "payments")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    public Loan(){}

    public Loan(String name, Integer maxAmount, List<Integer> payments, Double fee) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.fee = fee;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public void addclientloan (ClientLoan clientLoans1){
        clientLoans1.setLoan(this);
        clientLoans.add(clientLoans1);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public List<Client> getClient(){
        return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(toList());
    }
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}
