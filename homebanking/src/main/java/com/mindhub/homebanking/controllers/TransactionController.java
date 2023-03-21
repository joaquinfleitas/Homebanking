package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.ServicesAccount;
import com.mindhub.homebanking.Services.ServicesTransactions;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class TransactionController {
    @Autowired
    private ServicesTransactions servicesTransactions;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ServicesAccount servicesAccount;

    @GetMapping("/api/transactions")
    public List<TransactionDTO> getTransaction(){
        return servicesTransactions.findAll().stream().map(transaction -> new TransactionDTO(transaction)).collect(toList());
    }
    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> newTransfer(Authentication authentication

            , @RequestParam (required = false) Double amount, @RequestParam String description
            , @RequestParam String numberEmission, @RequestParam String numberReceptor) {

        Client client = clientService.findByEmail(authentication.getName());

        if (amount <= 0) {
            return new ResponseEntity<>("The amount must be greater than 0", HttpStatus.FORBIDDEN);
        }
         if (description.isEmpty()) {
            return new ResponseEntity<>("Missing Lastname", HttpStatus.FORBIDDEN);

        }  if (numberEmission.isEmpty()) {
            return new ResponseEntity<>("Missing email", HttpStatus.FORBIDDEN);

        }  if (numberReceptor.isEmpty()) {
            return new ResponseEntity<>("Missing Password", HttpStatus.FORBIDDEN);
        }
        if (numberEmission.equals(numberReceptor)){
            return new ResponseEntity<>("You can't send transfer at the same account", HttpStatus.BAD_REQUEST);
        }
        if (servicesAccount.findByNumber(numberEmission)==null){
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.NOT_FOUND);
        }
        if (client.getAccounts().stream().noneMatch(account -> account.getNumber().equals(numberEmission))){
            return new ResponseEntity<>("This account is not yours", HttpStatus.CONFLICT);
        }
        if (servicesAccount.findByNumber(numberReceptor)==null){
            return new ResponseEntity<>("la cuenta a la que intertas enviar no existe", HttpStatus.NOT_FOUND);
        }
        if (servicesAccount.findByNumber(numberEmission).getBalance() < amount){
            return new ResponseEntity<>("tus fondos no son suficientas para llevar a cabo esta transaccion", HttpStatus.NOT_FOUND);
        }
        Account numberEmision = servicesAccount.findByNumber(numberEmission);
        Account numbeReceptor = servicesAccount.findByNumber(numberReceptor);

        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, amount, description , LocalDateTime.now(), numberEmision.getBalance() - amount);
        Transaction transactionReceptor = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), numbeReceptor.getBalance() + amount);

        numberEmision.setBalance(numberEmision.getBalance()-amount);
        numbeReceptor.setBalance(numbeReceptor.getBalance()+amount);

        numberEmision.addTransaction(transactionOrigin);
        numbeReceptor.addTransaction(transactionReceptor);

        servicesAccount.save(numberEmision);
        servicesAccount.save(numbeReceptor);
        servicesTransactions.save(transactionOrigin);
        servicesTransactions.save(transactionReceptor);

        return new ResponseEntity<>("Your transaction was successful", HttpStatus.CREATED);
    }
}