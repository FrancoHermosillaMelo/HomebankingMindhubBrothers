package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.AccountDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.AccountType;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import com.mindhub.homebanking.Service.AccountService;
import com.mindhub.homebanking.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountController {
//        @Autowired
//        private AccountRepository repository;
//        @Autowired
//         private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;

//        private String randomNumber(){
//        String randomNumber;
//        do {
//            int number = (int) (Math.random() * 899999 + 100000);
//            randomNumber = "VIN-" + number;
//        } while (accountService.findByNumber(randomNumber) != null);
//        return randomNumber;
//    }

        @GetMapping("/api/accounts")
        public List<AccountDTO> getAccounts(){
            return accountService.getAccounts();
        }

        @GetMapping("/api/clients/current/accounts")
        public List<AccountDTO> getAccount(Authentication authentication) {
            return accountService.getAccountAuthentication(authentication);
        }
        @GetMapping("/api/clients/current/accounts/{id}")
        public AccountDTO getAccount(@PathVariable Long id){
            return accountService.getAccountID(id);
    }
//    clientRepository.findByEmail(authentication.getName()).getAccounts().size()

    @PostMapping("/api/clients/current/accounts")

    public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam AccountType accountType){
//        if (clientService.findByEmail(authentication.getName()).getAccounts().size() <= 2)
            Client client = clientService.findByEmail(authentication.getName());
        if(client == null) {
            return new ResponseEntity<>("You can't create an account because you're not a client.", HttpStatus.NOT_FOUND);
        }
                int totalAccounts = client.getAccounts().size();
                if (totalAccounts >= 6) {
                    return new ResponseEntity<>("Client already has the maximum number of accounts allowed.", HttpStatus.FORBIDDEN);
                }

                int activeAccounts = client.getAccounts().stream().filter(Account::getActiveAccount).collect(Collectors.toList()).size();

                if (activeAccounts >= 3) {
                    return new ResponseEntity<>("Client already has the maximum number of active accounts allowed.", HttpStatus.FORBIDDEN);
                }

                String accountNumber = accountService.randomNumberAccount();
                Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0.0, true, accountType);
                clientService.findByEmail(authentication.getName()).addAccount(newAccount);
                accountService.saveAccount(newAccount);


            return  new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping ("/api/accounts/{id}")
    public ResponseEntity<String> deleteAccount(Authentication authentication,@PathVariable long id) {

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);

        if (client == null) {
            return new ResponseEntity<>("You can't delete an account because you're not a client.", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        if (account.getBalance() != 0.0) {
            return new ResponseEntity<>("The account can't be deleted because it has a balance different from 0.", HttpStatus.FORBIDDEN);
        }

        account.setActiveAccount(false);
        accountService.saveAccount(account);

        List<Transaction> transactions = transactionRepository.findByAccountId(id);
        transactions.forEach(transaction -> {
            transaction.setActiveTransaction(false);
            transactionRepository.save(transaction);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
    }

