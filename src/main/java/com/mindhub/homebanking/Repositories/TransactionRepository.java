package com.mindhub.homebanking.Repositories;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(long id);
    @Query("SELECT t FROM Transaction t WHERE t.account =:account AND t.date >=:startDate AND t.date <=:endDate")
    List<Transaction> findByIdAndDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);
}
