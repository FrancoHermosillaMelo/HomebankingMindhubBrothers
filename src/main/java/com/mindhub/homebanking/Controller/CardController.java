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
import java.util.Set;
import java.util.stream.Collectors;
@RestController
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    private String randomNumberCards(){
        String randomCards = "";
        String randomCardsEnd = "";
        for (int i = 0; i < 4; i++){
            int min = 1000;
            int max = 8999;
            randomCards += (int) (Math.random() * max + min) + "-";

        }
        randomCardsEnd = randomCards.substring(0, randomCards.length()-1);
        return randomCardsEnd;
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
// cambiar
        do {
            cardsCvv = randomCvv();
        }while (cardRepository.findByCvv(cardsCvv) != null);

        Client selectClient = clientRepository.findByEmail(authentication.getName());

        Set<Card> cards = selectClient.getCards().stream().filter(card -> card.getType() == type).collect(Collectors.toSet());
        if (cards.stream().anyMatch(card -> card.getColor() == color)){
            return new ResponseEntity<>("You can't have same cards", HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(selectClient.getFirstName() + " " + selectClient.getLastName(),type,color,randomNumberCards(),randomCvv(), LocalDateTime.now(),LocalDateTime.now().plusYears(5));
        selectClient.addCard(newCard);
        cardRepository.save(newCard);

        return  new ResponseEntity<>(HttpStatus.CREATED);

    }
}
