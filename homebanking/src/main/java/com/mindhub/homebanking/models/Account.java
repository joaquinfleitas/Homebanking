package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;

    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Boolean mostrarAccount;

    private AccountType accountType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    public Account(){}

    public Account(String numbr, LocalDateTime creationDat,double balanc, Boolean mostrarAccount, AccountType accountType){
        this.number = numbr;
        this.creationDate = creationDat;
        this.balance = balanc;
        this.mostrarAccount = mostrarAccount;
        this.accountType = accountType;
    }

    public Boolean getMostrarAccount() {
        return mostrarAccount;
    }

    public void setMostrarAccount(Boolean mostrarAccount) {
        this.mostrarAccount = mostrarAccount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getNumber(){return number;}

    public void setNumber(String number){this.number = number;}
    public LocalDateTime getCreationDate(){return creationDate;}
    public void setCreationDate(LocalDateTime creationDate){this.creationDate = creationDate;}
    public double getBalance(){return balance;}
    public void setBalance(double balance){this.balance = balance;}

    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}

    public long getId() {
        return id;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    public Set<Transaction> getTransactions() {
        return transactions;
    }
}
