package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.CardDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.CardRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Service.CardService;
import com.mindhub.homebanking.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
public class CardController {
//    @Autowired
//    private CardRepository cardRepository;
//    @Autowired
//    private ClientRepository clientRepository;
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

//    private String randomNumberCards(){
//        String randomCards = "";
//        String randomCardsEnd = "";
//        for (int i = 0; i < 4; i++){
//            int min = 1000;
//            int max = 8999;
//            randomCards += (int) (Math.random() * max + min) + "-";
//
//        }
//        randomCardsEnd = randomCards.substring(0, randomCards.length()-1);
//        return randomCardsEnd;
//    }
//    private int randomCvv(){
//        int cvvRandom = (int)(Math.random()*899 + 100);
//        return cvvRandom;
//    }

    @GetMapping("/api/clients/current/cards")
    public List<CardDTO> getCard (Authentication authentication){
        return cardService.getCard(authentication);
    }

    @PostMapping("/api/clients/current/cards")

    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardType type , @RequestParam CardColor color){

        if (type  == null || color  == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        String cardsNumber = cardService.cardNumberNotRepeat();

        Client selectClient = clientService.findByEmail(authentication.getName());

        Set<Card> cards = selectClient.getCards().stream().filter(card -> card.getType() == type && card.getActive()).collect(Collectors.toSet());

        int cardsTotal = selectClient.getCards().size();
        int cardsActive = (int) selectClient.getCards().stream().filter(card -> card.getActive()).count();

        if (cardsTotal >= 8 || cardsActive >= 8) {
            return new ResponseEntity<>("You reached the maximum number of cards you can have", HttpStatus.FORBIDDEN);
        }
        if (cards.stream().anyMatch(card -> card.getColor() == color && card.getActive())){
            return new ResponseEntity<>("You can't have same cards", HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(selectClient.getFirstName() + " " + selectClient.getLastName(),type,color,cardsNumber,cardService.randomCvv(), LocalDateTime.now(),LocalDateTime.now().plusYears(5), true);
        selectClient.addCard(newCard);
        cardService.saveCard(newCard);

        return  new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PutMapping(path = "/api/clients/current/cards/{id}")
    public ResponseEntity<Object> cardDelete (Authentication authentication, @PathVariable Long id){
        Client clientAuthentication = clientService.findByEmail(authentication.getName());
        Card cardID = cardService.findById(id);

       if(!cardID.getActive()){
           return new ResponseEntity<>("This card is deleted", HttpStatus.FORBIDDEN);
       }
       if (!clientAuthentication.getCards().contains(cardID)){
           return new ResponseEntity<>("This card does not belong to you", HttpStatus.FORBIDDEN);
       }
       if (cardID == null){
           return new ResponseEntity<>("ID does not exist", HttpStatus.FORBIDDEN);
       }

       cardID.setActive(false);
       cardService.saveCard(cardID);

       return new ResponseEntity<>("The card was deleted", HttpStatus.ACCEPTED);
    }
}
