package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.ServicesAccount;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utilities.utils.Number2;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
public class AccountController {
    @Autowired
    private ServicesAccount servicesAccount;
    @Autowired
    private ClientService clientService;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return servicesAccount.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @GetMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return new AccountDTO(servicesAccount.findById(id).orElse(null));
    }

    @GetMapping("/api/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        Client clienteAutenticado = clientService.findByEmail(authentication.getName());
        List<Account> accountTrue = clienteAutenticado.getAccounts().stream().filter(account -> account.getMostrarAccount() == true).collect(toList());
      return accountTrue.stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam String accountType) {
        Client client = clientService.findByEmail(authentication.getName());
        AccountType accountType1 = AccountType.valueOf(accountType);

        if(client.getAccounts().stream().filter(account -> account.getMostrarAccount() == true).collect(toSet()).size() > 3){
            return new ResponseEntity<>("ya tienes 3 cuentas. Alcanzaste el maximo", HttpStatus.FORBIDDEN);
        }
        Account newAccount= new Account(Number2(servicesAccount), LocalDateTime.now(), 0, true, accountType1);
        client.addAccount(newAccount);
        servicesAccount.save(newAccount);

        return new ResponseEntity<>("Your account is created successful" , HttpStatus.CREATED);
    }
    @PatchMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> borrarCuenta(Authentication authentication, @RequestParam String name){
        Client clienteAutenticado = clientService.findByEmail(authentication.getName());
        Account ocultarCuenta = servicesAccount.findByNumber(name);

        if (name.isEmpty()){
            return new ResponseEntity<>("name account is empty", HttpStatus.BAD_REQUEST);
        }
        if (!clienteAutenticado.getLoan().isEmpty()){
            return new ResponseEntity<>("You can't delete account because you need pay all loans", HttpStatus.FORBIDDEN);
        }

        ocultarCuenta.setMostrarAccount(false);
        servicesAccount.save(ocultarCuenta);
        clientService.save(clienteAutenticado);

        return new ResponseEntity<>("Successfully", HttpStatus.CREATED);
    };

    /*public String GenerateNumber(){
        int number1=(int) (Math.random() * (99999999));
        String number ="VIN-"+number1;
        return number;
    }

    public String Number(AccountRepositories accountRepositories){
        String Number;
        boolean verifyNumber;
        do {
            Number=GenerateNumber();
            verifyNumber=accountRepositories.existsByNumber(Number);
        }while(verifyNumber);
        return Number;
    }*/

}

