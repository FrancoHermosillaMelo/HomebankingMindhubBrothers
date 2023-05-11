package com.mindhub.homebanking.DTOS;

import com.mindhub.homebanking.Models.ClientLoan;


public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String name;
    private double amount;
    private int payments;
    private double totalAmount;


    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.totalAmount = clientLoan.getTotalAmount();
    }

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
