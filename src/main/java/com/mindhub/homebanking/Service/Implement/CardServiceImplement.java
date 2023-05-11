package com.mindhub.homebanking.Service.Implement;

import com.mindhub.homebanking.DTOS.CardDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Repositories.CardRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<CardDTO> getCard(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getCards().stream().collect(Collectors.toList());
    }
    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
    @Override
    public Card findByCvv(int cvv) {
        return cardRepository.findByCvv(cvv);
    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
