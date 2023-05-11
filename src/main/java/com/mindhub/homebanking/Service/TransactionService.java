package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.DTOS.TransactionDTO;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    List<TransactionDTO> findByIdAndDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);
}
