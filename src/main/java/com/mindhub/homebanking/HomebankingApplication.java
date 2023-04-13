package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	LocalDateTime day = LocalDateTime.now();
	List<Integer> mortgage = List.of(12, 24, 36, 48, 60);
	List<Integer> personal = List.of(6, 12, 24);
	List<Integer> automotive = List.of(6, 12, 24, 36);
	@Bean
	public CommandLineRunner initData(ClientRepository  repository, AccountRepository Repository, TransactionRepository repository3, LoanRepository RepositoryLoan, ClientLoanRepository RepositoryClientLoan) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Franco", "Hermosilla", "franco@gmail.com");
			repository.save(client1);
			repository.save(client2);

			Account account1 = new Account("VIN001", day  , 5000.0);
			Account account2 = new Account("VIN002", day.plusDays(1), 7500.0);
			client1.addAccount(account1);
			client1.addAccount(account2);
			Repository.save(account1);
			Repository.save(account2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1000.58,"Clothes", day);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 2500.22,"Food", day.plusDays(1));
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 957.24,"Cinema", day.plusDays(1));
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 526.14,"Food", day.plusDays(2));
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			repository3.save(transaction1);
			repository3.save(transaction2);
			repository3.save(transaction3);
			repository3.save(transaction4);


			Loan loan1 = new Loan("MORTGAGE", 500000, mortgage );
			Loan loan2 = new Loan("PERSON", 100000, personal );
			Loan loan3 = new Loan("AUTOMOTIVE", 300000, automotive);
			RepositoryLoan.save(loan1);
			RepositoryLoan.save(loan2);
			RepositoryLoan.save(loan3);

			ClientLoan clientloan1 = new ClientLoan("MORTGAGE", 400000,  60);
			client1.addClientLoan(clientloan1);
			loan1.addClientLoan(clientloan1);

			ClientLoan clientloan2 = new ClientLoan("PERSONAL", 50000, 12);
			client1.addClientLoan(clientloan2);
			loan2.addClientLoan(clientloan2);

			ClientLoan clientloan3 = new ClientLoan("PERSONAL", 100000, 24);
			client2.addClientLoan(clientloan3);
			loan2.addClientLoan(clientloan3);

			ClientLoan clientloan4 = new ClientLoan( "AUTOMOTIVE", 200000, 36);
			client2.addClientLoan(clientloan4);
			loan3.addClientLoan(clientloan4);

			RepositoryClientLoan.save(clientloan1);
			RepositoryClientLoan.save(clientloan2);
			RepositoryClientLoan.save(clientloan3);
			RepositoryClientLoan.save(clientloan4);



		};
	}

}
