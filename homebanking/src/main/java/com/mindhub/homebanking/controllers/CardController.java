package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.ServicesAccount;
import com.mindhub.homebanking.Services.ServicesCard;
import com.mindhub.homebanking.Services.ServicesTransactions;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.PosnetDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utilities.utils.*;
import static com.mindhub.homebanking.models.CardType.DEBIT;
import static java.util.stream.Collectors.toList;

@RestController
public class CardController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ServicesCard servicesCard;
    @Autowired
    private ServicesAccount servicesAccount;
    @Autowired
    private ServicesTransactions servicesTransactions;
    @GetMapping("/api/clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication) {
        //condicional que no tome las cards con la propiedad en false antes de generar los DTO//
        Client clienteAutenticad = clientService.findByEmail(authentication.getName());
        List<Card> TarjetasTrue = clienteAutenticad.getCards().stream().filter(card -> card.getMostrarTarjeta() == true).collect(Collectors.toList());

        return TarjetasTrue.stream().map(card -> new CardDTO(card)).collect(toList());
    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> newDebitCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {
        Client clientAutenticado = clientService.findByEmail(authentication.getName());

        if (clientAutenticado.getCards().stream().filter(card -> card.getMostrarTarjeta() == true).collect(toList()).size() > 6) {
            return new ResponseEntity<>("you exceed the card limit", HttpStatus.FORBIDDEN);
        }
        if (clientAutenticado.getCards().stream().anyMatch(card -> cardType == card.getCardType() && cardColor == card.getCardColor() && card.getMostrarTarjeta() == true)) {
            return new ResponseEntity<>("This card " + cardType + " " + cardColor + " is already created", HttpStatus.BAD_REQUEST);
        }

        Card newCard = new Card(clientAutenticado.getFirstName() + " " + clientAutenticado.getLastName(), Number3(servicesCard), cvv(), cardType, cardColor, LocalDate.now().plusYears(5), LocalDate.now(), true);
        clientAutenticado.addCard(newCard);
        servicesCard.save(newCard);

        return new ResponseEntity<>("Card created successful", HttpStatus.CREATED);
    }

    @PatchMapping("/api/clients/current/cards")
    public ResponseEntity<Object> borrarTarjeta(Authentication authentication, @RequestParam String number) {
        Client clienteAutenticado = clientService.findByEmail(authentication.getName());
        Card traerTarjetaBorrada = servicesCard.findByNumber(number);

        if (number.isEmpty()) {
            return new ResponseEntity<>("number card is empty", HttpStatus.BAD_REQUEST);
        }
        if (clienteAutenticado.getCards().stream().noneMatch(card -> card.getNumber().equals(number))) {
            return new ResponseEntity<>("this card is'nt yours", HttpStatus.FORBIDDEN);
        }

        traerTarjetaBorrada.setMostrarTarjeta(false);
        servicesCard.save(traerTarjetaBorrada);
        return new ResponseEntity<>("ups you really sucks gg ezpz tu vieja tu vieja en bisi", HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @Transactional
    @PostMapping("/api/payment/debit")
    public ResponseEntity <Object> payCards(
            @RequestBody(required = false) PosnetDTO posnetDTO){
        Card card = servicesCard.findByNumber(posnetDTO.getNumber());
        if (posnetDTO.getNumber().isEmpty()){
            return new ResponseEntity<>("The card number is missing",HttpStatus.BAD_REQUEST);}
        if (posnetDTO.getAmount() == null && posnetDTO.getAmount() > 0){
            return new ResponseEntity<>("The amount to be paid is missing",HttpStatus.BAD_REQUEST);}
        if (posnetDTO.getCvv().isEmpty())
            return new ResponseEntity<>("The card cvv is missing",HttpStatus.BAD_REQUEST);
        if (posnetDTO.getDescription().isEmpty())
            return new ResponseEntity<>("The description is missing",HttpStatus.BAD_REQUEST);
        if (card == null)
            return new ResponseEntity<>("Card not found",HttpStatus.BAD_REQUEST);
        if (!card.getMostrarTarjeta())
            return new ResponseEntity<>("The card is not valid",HttpStatus.BAD_REQUEST);
        if (card.getCardType() != DEBIT)
            return new ResponseEntity<>("The card is not debit",HttpStatus.BAD_REQUEST);
        if (card.getThruDate().isBefore(card.getThruDate()))
            return new ResponseEntity<>("The card has expired",HttpStatus.BAD_REQUEST);
        Account account = card.getClient().getAccounts().stream().iterator().next();
        if (account == null)
            return new ResponseEntity<>("There is no associated account",HttpStatus.BAD_REQUEST);
        if (!account.getMostrarAccount())
            return new ResponseEntity<>("The associated account is inactive",HttpStatus.BAD_REQUEST);
        if (account.getBalance()<posnetDTO.getAmount())
            return new ResponseEntity<>("Your account balance does not cover the payment",HttpStatus.BAD_REQUEST);

        Transaction transaction = new Transaction(TransactionType.DEBIT,- posnetDTO.getAmount(), posnetDTO.getDescription(), LocalDateTime.now(),account.getBalance() - posnetDTO.getAmount());
        account.setBalance(account.getBalance() - posnetDTO.getAmount());
        account.addTransaction(transaction);
        servicesTransactions.save(transaction);
        servicesAccount.save(account);
        return new ResponseEntity<>("Payment made", HttpStatus.ACCEPTED);
    }

    /*@GetMapping("/clients/nuevatarjeta")
    public List<CardDTO> meDevuelveUnaListaDeCardDTO(Authentication authentication){
          Client clientt = clientRepositories.findByEmail(authentication.getName());

         return clientt.getCards().stream().map(card -> new CardDTO(card)).collect(toList());
    }*/

}
