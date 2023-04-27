package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.CardDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardColor;
import com.mindhub.homebanking.Models.CardType;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.CardRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@RestController
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    private String randomNumberCards(){
        String randomCards = "";
        for (int i = 0; i < 4; i++){
            int min = 1000;
            int max = 8999;
            randomCards += (int) (Math.random() * max + min) + "-";
        }
        return randomCards;
    }
    private int randomCvv(){
        int cvvRandom = (int)(Math.random()*899 + 100);
        return cvvRandom;
    }
    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO> getAccounts (Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getCards().stream().collect(Collectors.toList());
    }

    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.POST)

    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardType type , @RequestParam CardColor color){

        if (type  == null || color  == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        String cardsNumber;
        do {
             cardsNumber = randomNumberCards();
        }while(cardRepository.findByNumber(cardsNumber) != null);

        int cardsCvv;

        do {
            cardsCvv = randomCvv();
        }while (cardRepository.findByCvv(cardsCvv) != null);

        Client selectClient = clientRepository.findByEmail(authentication.getName());

        for (Card card : selectClient.getCards()) {
            if (card.getType().equals(type) && card.getColor().equals((color))) {
                return new ResponseEntity<>("You already have this color and type of card", HttpStatus.FORBIDDEN);
            }
        }

        Card newCard = new Card(selectClient.getFirstName() + " " + selectClient.getLastName(),type,color,randomNumberCards(),randomCvv(), LocalDateTime.now(),LocalDateTime.now().plusYears(5));
        clientRepository.findByEmail(authentication.getName()).addCard(newCard);
        cardRepository.save(newCard);

        return  new ResponseEntity<>(HttpStatus.CREATED);

    }
}
