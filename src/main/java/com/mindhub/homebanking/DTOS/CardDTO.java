package com.mindhub.homebanking.DTOS;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardColor;
import com.mindhub.homebanking.Models.CardType;
import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private Boolean active;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.active = card.getActive();
    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public Boolean getActive() {
        return active;
    }
}
