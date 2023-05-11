package com.mindhub.homebanking.Service.Implement;

import com.mindhub.homebanking.DTOS.TransactionDTO;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import com.mindhub.homebanking.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDTO> findByIdAndDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByIdAndDateBetween(account, startDate, endDate).stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }
}
