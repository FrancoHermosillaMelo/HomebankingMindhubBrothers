package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	LocalDateTime day = LocalDateTime.now();
	@Bean
	public CommandLineRunner initData(ClientRepository  repository, AccountRepository Repository, TransactionRepository repository3) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Franco", "Melo", "franco@gmail.com");
			Client client3 = new Client("Nicol", "Sanchez", "nicol@gmail.com");
			repository.save(client1);
			repository.save(client2);
			repository.save(client3);
			Account account1 = new Account("VIN001", day  , 5000.0, client1);
			Account account2 = new Account("VIN002", day.plusDays(1), 7500.0, client1);
			Account account3 = new Account("VIN003", day, 5000.0, client2);
			Account account4 = new Account("VIN004", day, 10000.0, client3);
			Repository.save(account1);
			Repository.save(account2);
			Repository.save(account3);
			Repository.save(account4);
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1000.58,"Clothes", day, account1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 2500.22,"Food", day.plusDays(1), account1);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 957.24,"Cinema", day.plusDays(1), account2);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 526.14,"Food", day.plusDays(2), account2);
			repository3.save(transaction1);
			repository3.save(transaction2);
			repository3.save(transaction3);
			repository3.save(transaction4);
		};
	}

}
