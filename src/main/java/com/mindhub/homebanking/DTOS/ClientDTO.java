package com.mindhub.homebanking.DTOS;
import com.mindhub.homebanking.Models.Client;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private Set<AccountDTO> account = new HashSet<>();

    private Set<ClientLoanDTO> loans = new HashSet<>();

    private Set<CardDTO> cards = new HashSet<>();

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.account = client.getAccounts()
                .stream()
                .filter(account1 -> account1.getActiveAccount())
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
        this.loans = client.getClientloans().stream().map(clientloan -> new ClientLoanDTO(clientloan)).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }
    public Set<AccountDTO> getAccount() {
        return account;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }

}
