package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


public class ClientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountDTO> accounts;
    private List<ClientLoanDTO> loans;
    private List<CardDTO> cards;

    public ClientDTO(){}

    public ClientDTO(Client client){
        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toList());

        this.loans = client.getClientLoans().stream().map(loans -> new ClientLoanDTO(loans)).collect(toList());

        this.cards = client.getCards().stream().map(cards -> new CardDTO(cards)).collect(toList());
    }


    public long getId() {return id;}
    public void setId(long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getEmail(){
        return email;
    }
    public  String getLastName(){
        return lastName;
    }
    public List<ClientLoanDTO> getLoans() {return loans;}
    public List<AccountDTO> getAccounts(){return accounts;}
    public List<CardDTO> getCards(){return cards;}
}




