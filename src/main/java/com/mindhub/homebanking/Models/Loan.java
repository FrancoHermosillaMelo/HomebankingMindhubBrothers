package com.mindhub.homebanking.Models;
import com.mindhub.homebanking.DTOS.ClientLoanDTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private double maxAmount;
    private double interests;
    @ElementCollection
    @Column(name= "payment")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientloans = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments, double interests) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interests = interests;
    }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientloans.add(clientLoan);
    }
    public List<Client> getClients() {
        return clientloans.stream().map(clientLoan -> clientLoan.getClient()).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }


    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientloans() {
        return clientloans;
    }

    public void setClientloans(Set<ClientLoan> clientloans) {
        this.clientloans = clientloans;
    }

    public double getInterests() {
        return interests;
    }

    public void setInterests(double interests) {
        this.interests = interests;
    }
}
