package com.mindhub.homebanking.Controller;


import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import com.mindhub.homebanking.Service.AccountService;
import com.mindhub.homebanking.Service.ClientService;
import com.mindhub.homebanking.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestController
public class TransactionController {

//    @Autowired
//    private ClientRepository clientRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

@Transactional
    @RequestMapping(path = "/api/clients/current/transaction", method = RequestMethod.POST)

    public ResponseEntity<Object> newTransaction(Authentication authentication
            , @RequestParam double amount, @RequestParam String description
            , @RequestParam String account1, @RequestParam String account2){

        Client selectClient = clientService.findByEmail(authentication.getName());
        Account selectAccount1 = accountService.findByNumber(account1.toUpperCase());
        Account selectAccount2 = accountService.findByNumber(account2.toUpperCase());

        if (amount < 1){
            return new ResponseEntity<>("You cannot send negative amounts", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Please add a description", HttpStatus.FORBIDDEN);
        }
        if (selectAccount1 == null){
            return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccount1.equals(selectAccount2)){
            return new ResponseEntity<>("You cannot send money to the same account", HttpStatus.FORBIDDEN);
        }
        if (selectAccount2 == null){
            return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccount1.getBalance() < amount){
            return new ResponseEntity<>("I do not have enough money", HttpStatus.FORBIDDEN);
        }
        if (selectClient.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(account1)).collect(Collectors.toList()).size() == 0){
            return new ResponseEntity<>("This account is not yours", HttpStatus.FORBIDDEN);
        }

        selectAccount1.setBalance(selectAccount1.getBalance() - amount);
        selectAccount2.setBalance(selectAccount2.getBalance() + amount);

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now());
        selectAccount1.addTransaction(newTransaction);
       transactionService.saveTransaction(newTransaction);

        Transaction newTransactionCredit = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now());
        selectAccount2.addTransaction(newTransactionCredit);
        transactionService.saveTransaction(newTransactionCredit);



        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
