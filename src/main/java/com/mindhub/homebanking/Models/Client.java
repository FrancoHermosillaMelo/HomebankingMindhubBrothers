package com.mindhub.homebanking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
      @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
        private Set<Account> accounts = new HashSet<>();
      @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
        private Set<ClientLoan> clientloans = new HashSet<>();

      @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
      private Set<Card> cards = new HashSet<>();

        public Client() {
        }

        public Client(String firstName, String lastName, String email, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }

        public void addAccount(Account account){
            account.setClient(this);
            accounts.add(account);
        }

        public void addClientLoan(ClientLoan clientLoan){
            clientLoan.setClient(this);
            clientloans.add(clientLoan);
        }

        public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
        }
    @JsonIgnore
    public List<Loan> getLoans() {
        return clientloans.stream().map(clientLoan -> clientLoan.getLoan()).collect(Collectors.toList());
    }

    public String getFirstName() {
            return firstName;
        }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public long getId() {
            return id;
        }

    public Set<ClientLoan> getClientloans() {
        return clientloans;
    }

    public void setClientloans(Set<ClientLoan> clientloans) {
        this.clientloans = clientloans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
