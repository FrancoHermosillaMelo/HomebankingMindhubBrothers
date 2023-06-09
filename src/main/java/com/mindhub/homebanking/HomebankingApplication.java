package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner initData(ClientRepository  repository, AccountRepository Repository, TransactionRepository repository3, LoanRepository RepositoryLoan, ClientLoanRepository RepositoryClientLoan, CardRepository RepositoryCard) {
		return (args) -> {
			LocalDateTime day = LocalDateTime.now();
			List<Integer> mortgage = List.of(12, 24, 36, 48, 60);
			List<Integer> personal = List.of(6, 12, 24);
			List<Integer> automotive = List.of(6, 12, 24, 36);

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("asd12345"));
			Client client2 = new Client("Franco", "Hermosilla", "francohermosilla@gmail.com", passwordEncoder.encode("asd54321"));
			repository.save(client1);
			repository.save(client2);

			Account account1 = new Account("VIN-001", day  , 5000.0, true,AccountType.CURRENT);
			Account account2 = new Account("VIN-002", day.plusDays(1), 7500.0,true,AccountType.SAVING);
			client1.addAccount(account1);
			client1.addAccount(account2);
			Repository.save(account1);
			Repository.save(account2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1000.58,"Clothes", day.minusDays(1),true,account1.getBalance());
			account1.setBalance(account1.getBalance() - 2500.22);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 957.24,"Cinema", day.minusDays(1),true,account2.getBalance());
			account2.setBalance(account2.getBalance() - 526.14);
			account1.addTransaction(transaction1);
			account2.addTransaction(transaction3);
			repository3.save(transaction1);
			repository3.save(transaction3);



			Loan loan1 = new Loan("MORTGAGE", 500000, mortgage, 1.3 );
			Loan loan2 = new Loan("PERSON", 100000, personal, 1.1 );
			Loan loan3 = new Loan("AUTOMOTIVE", 300000, automotive, 1.2);
			RepositoryLoan.save(loan1);
			RepositoryLoan.save(loan2);
			RepositoryLoan.save(loan3);

			ClientLoan clientloan1 = new ClientLoan( 400000,  60, 400000 * loan1.getInterests());
			client1.addClientLoan(clientloan1);
			loan1.addClientLoan(clientloan1);

			ClientLoan clientloan2 = new ClientLoan( 50000, 12,50000 * loan2.getInterests());
			client1.addClientLoan(clientloan2);
			loan2.addClientLoan(clientloan2);

			ClientLoan clientloan3 = new ClientLoan( 100000, 24,100000 * loan2.getInterests());
			client2.addClientLoan(clientloan3);
			loan2.addClientLoan(clientloan3);

			ClientLoan clientloan4 = new ClientLoan(  200000, 36,200000 * loan3.getInterests());
			client2.addClientLoan(clientloan4);
			loan3.addClientLoan(clientloan4);

			RepositoryClientLoan.save(clientloan1);
			RepositoryClientLoan.save(clientloan2);
			RepositoryClientLoan.save(clientloan3);
			RepositoryClientLoan.save(clientloan4);

			Card card1 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.DEBIT, CardColor.GOLD, "4787-3459-2485-4715", 745, day.minusYears(5), day.minusDays(1), true);
			Card card2 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM,"3512-5488-4264-7175", 841, day.minusYears(5), day.minusDays(1), true);
			Card card3 = new Card("Franco Hermosilla", CardType.CREDIT, CardColor.SILVER, "3750-6120-4687-3347", 514, day, day.plusYears(5), true);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			RepositoryCard.save(card1);
			RepositoryCard.save(card2);
			RepositoryCard.save(card3);

		};
	}

}
