package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
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
public class LoanController {
    @Autowired
    private ServicesAccount servicesAccount;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ServicesClientLoan servicesClientLoan;
    @Autowired
    private ServicesLoan servicesLoan;
    @Autowired
    private ServicesTransactions servicesTransactions;

    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans(){
        return servicesLoan.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }
    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> postLoan(Authentication authentication, @RequestBody(required = false) LoanApplicationDTO loanApplicationDTO){

        Client clientAutenticado = clientService.findByEmail(authentication.getName());
        Loan loan = servicesLoan.findById(loanApplicationDTO.getId()).get();
        Account accountBalance = servicesAccount.findByNumber(loanApplicationDTO.getNumberAccountDestiny());

            if(loanApplicationDTO.getId() == null || loanApplicationDTO.getId() == 0){
                return new ResponseEntity<>("Bad request", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getAmount()== null || loanApplicationDTO.getAmount() <= 0){
                return new ResponseEntity<>("You need more amount", HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getPayments() == null || loanApplicationDTO.getPayments() <= 0){
                return new ResponseEntity<>("Payments is empty", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getNumberAccountDestiny() == null){
                return new ResponseEntity<>("your account has no addressee", HttpStatus.FORBIDDEN);
            }
             if (loanApplicationDTO.getAmount() <= 0){
                 return new ResponseEntity<>("NO SE PUEDE", HttpStatus.BAD_REQUEST);}

            if (loanApplicationDTO.getPayments() <= 0){
                return new ResponseEntity<>("TAMPOCO", HttpStatus.BAD_REQUEST);}

            if(servicesLoan.getReferenceById(loanApplicationDTO.getId()).getMaxAmount() < loanApplicationDTO.getAmount()){
                return new ResponseEntity<>("Amount exceeds your loan", HttpStatus.FORBIDDEN);
            }
            if(!servicesLoan.getReferenceById(loanApplicationDTO.getId()).getPayments().contains(loanApplicationDTO.getPayments())){
                return new ResponseEntity<>("Payment option non exist", HttpStatus.FORBIDDEN);
            }
            if(!clientAutenticado.getAccounts().contains(servicesAccount.findByNumber(loanApplicationDTO.getNumberAccountDestiny()))){
                return new ResponseEntity<>("The destination account does not belong to you", HttpStatus.FORBIDDEN);
            }
            if (clientAutenticado.getLoan().contains(servicesLoan.findById(loanApplicationDTO.getId()).orElse(null))){
            return new ResponseEntity<>("you already have this loan", HttpStatus.BAD_REQUEST);
            }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * loan.getFee(), loanApplicationDTO.getPayments(), loan, clientAutenticado);
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), servicesLoan.getReferenceById(loanApplicationDTO.getId()).getName() + " Loan Approve", LocalDateTime.now(), accountBalance.getBalance() + loanApplicationDTO.getAmount());

        servicesLoan.findById(loanApplicationDTO.getId()).get().addclientloan(clientLoan);
        servicesAccount.findByNumber(loanApplicationDTO.getNumberAccountDestiny()).addTransaction(transaction);
        servicesAccount.findByNumber(loanApplicationDTO.getNumberAccountDestiny()).setBalance(accountBalance.getBalance() + loanApplicationDTO.getAmount());
        servicesClientLoan.save(clientLoan);
        clientService.save(clientAutenticado);
        servicesLoan.save(loan);
        servicesTransactions.save(transaction);

            return new ResponseEntity<>("Your loan was successful", HttpStatus.CREATED);
    }

    @PostMapping("/api/admin/loan")
    public ResponseEntity<Object> crearLoan(@RequestParam String name,
                                            @RequestParam Integer maxAmount,
                                            @RequestParam List<Integer> payments,
                                            @RequestParam Double fee){
    if(name.isEmpty()){
        return new ResponseEntity<>("missing name", HttpStatus.BAD_REQUEST);
    }
    if (maxAmount.toString().isEmpty()){
        return new ResponseEntity<>("missing maxAmount", HttpStatus.BAD_REQUEST);
    }
    if (payments.isEmpty()){
        return new ResponseEntity<>("missing Payments", HttpStatus.BAD_REQUEST);
    }
    if (fee.isNaN()){
        return new ResponseEntity<>("missing Fee", HttpStatus.BAD_REQUEST);
    }
    if(servicesLoan.findAll().stream().anyMatch(loan -> loan.getName().equals(name))){
        return new ResponseEntity<>("You can't created the same loan", HttpStatus.BAD_REQUEST);
    }
    Loan loan = new Loan(name, maxAmount, payments, fee);
    servicesLoan.save(loan);
    return new ResponseEntity<>("Loan created", HttpStatus.CREATED);
    }

}
