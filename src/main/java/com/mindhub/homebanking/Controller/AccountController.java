package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.AccountDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
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
        @Autowired
        private AccountRepository repository;
        @Autowired
         private ClientRepository clientRepository;

        private String randomNumber(){
        String randomNumber;
        do {
            int number = (int) (Math.random() * 899999 + 100000);
            randomNumber = "VIN-" + number;
        } while (repository.findByNumber(randomNumber) != null);
        return randomNumber;
    }

        @RequestMapping("/api/accounts")
        public List<AccountDTO> getAccounts(){
            return repository.findAll().stream().map(accounts -> new AccountDTO(accounts)).collect(Collectors.toList());
        }

        @RequestMapping("/api/clients/current/accounts")
        public List<AccountDTO> getAccount(Authentication authentication) {
            return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getAccount().stream().collect(Collectors.toList());
        }
        @RequestMapping("/api/clients/current/accounts/{id}")
        public AccountDTO getAccount(@PathVariable Long id){
            return repository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> newAccount(Authentication authentication){
            if (clientRepository.findByEmail(authentication.getName()).getAccounts().size() <= 2){
                String accountNumber = randomNumber();
                Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0.0);
                clientRepository.findByEmail(authentication.getName()).addAccount(newAccount);
                repository.save(newAccount);
    }else{
                return  new ResponseEntity<>("You reached the limit of accounts", HttpStatus.FORBIDDEN);
            }
            return  new ResponseEntity<>(HttpStatus.CREATED);

    }
    }

