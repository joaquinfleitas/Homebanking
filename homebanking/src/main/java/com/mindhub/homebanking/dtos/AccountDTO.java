package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private List<TransactionDTO> transaction;
    private AccountType accountType;

    public AccountDTO(Account account){
        this.id = account.getId();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.number = account.getNumber();
        this.transaction = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toList());
        this.accountType = account.getAccountType();
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public long getId() {return id;}

    public String getNumber() {return number;}
    public LocalDateTime getCreationDate() {return creationDate;}
    public double getBalance() {return balance;}

    public List<TransactionDTO> getTransaction() {return transaction;}

}
