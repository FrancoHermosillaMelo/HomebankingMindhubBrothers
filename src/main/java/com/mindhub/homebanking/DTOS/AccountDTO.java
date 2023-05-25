package com.mindhub.homebanking.DTOS;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Set<TransactionDTO> transactionDTOS;
    private Boolean activeAccount;
    private AccountType typeAccount;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactionDTOS = account.getTransactions()
                .stream()
                .filter(transaction -> transaction.getActiveTransaction())
                .map(transaction -> new TransactionDTO(transaction))
                .collect(Collectors.toSet());
        this.activeAccount = account.getActiveAccount();
        this.typeAccount = account.getTypeAccount();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactionDTOS() {
        return transactionDTOS;
    }

    public Boolean getActiveAccount() {
        return activeAccount;
    }

    public AccountType getTypeAccount() {
        return typeAccount;
    }
}
