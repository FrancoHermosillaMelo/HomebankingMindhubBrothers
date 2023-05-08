package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.DTOS.CardDTO;
import com.mindhub.homebanking.Models.Card;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface CardService {
    List<CardDTO> getCard(Authentication authentication);
    Card findByNumber(String number);
    Card findByCvv(int cvv);
    void saveCard(Card card);
}
