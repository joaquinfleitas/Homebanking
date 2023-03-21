package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.repositories.CardRepository;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();    //hashset se guarda en la memoria de la aplicacion java.

    @OneToMany(mappedBy="client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy="client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client(){}

    public Client(String first, String last, String email, String password){
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.password = password;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }
    public void addClientLoan(ClientLoan clientLoans1){
        clientLoans1.setClient(this);
        clientLoans.add(clientLoans1);
    }
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    };

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {return id;}
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public  String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Account> getAccounts(){return accounts;}
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards(){return cards;}

    @JsonIgnore
    public List<Loan>getLoan(){
      return clientLoans.stream().map(clientLoan -> clientLoan.getLoan()).collect(toList());
    };
    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public String toString(){
        return firstName + " " + lastName + " " + email;
    }

}
