package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.ServicesAccount;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.mindhub.homebanking.Utilities.utils.genericNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


@RestController
public class ClientController{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ServicesAccount servicesAccount;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClients(){
        return clientService.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @GetMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientService.findById(id).orElse(null));
    }

    @PostMapping("/api/clients")
    public ResponseEntity<Object> register(

            @RequestParam String first, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (first.isEmpty()) {
            return new ResponseEntity<>("Missing FirstName", HttpStatus.BAD_REQUEST);
        }
        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Missing LastName", HttpStatus.BAD_REQUEST);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("Missing Email", HttpStatus.BAD_REQUEST);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("Missing Password", HttpStatus.BAD_REQUEST);
        }
        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(first, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(genericNumber(), LocalDateTime.now(), 0, true, AccountType.CURRENT);
        client.addAccount(account);
        clientService.save(client);
        servicesAccount.save(account);

        return new ResponseEntity<>("Welcome "+first+" "+lastName, HttpStatus.CREATED);
    }
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/api/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        Client clienteAutenticado = clientService.findByEmail(authentication.getName());
        Set<Card> TarjetasTrue = clienteAutenticado.getCards().stream().filter(card -> card.getMostrarTarjeta() == true).collect(toSet());
        clienteAutenticado.setCards(TarjetasTrue);

        Set<Account> accountTrue = clienteAutenticado.getAccounts().stream().filter(account -> account.getMostrarAccount() == true).collect(toSet());
        clienteAutenticado.setAccounts(accountTrue);
        return new ClientDTO(clienteAutenticado);
    }


}

