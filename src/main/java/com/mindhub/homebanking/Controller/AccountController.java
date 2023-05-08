package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.AccountDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
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

        private String randomNumber(){
        String randomNumber;
        do {
            int number = (int) (Math.random() * 899999 + 100000);
            randomNumber = "VIN-" + number;
        } while (accountService.findByNumber(randomNumber) != null);
        return randomNumber;
    }

        @RequestMapping("/api/accounts")
        public List<AccountDTO> getAccounts(){
            return accountService.getAccounts();
        }

        @RequestMapping("/api/clients/current/accounts")
        public List<AccountDTO> getAccount(Authentication authentication) {
            return accountService.getAccountAuthentication(authentication);
        }
        @RequestMapping("/api/clients/current/accounts/{id}")
        public AccountDTO getAccount(@PathVariable Long id){
            return accountService.getAccountID(id);
    }
//    clientRepository.findByEmail(authentication.getName()).getAccounts().size()

    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)

    public ResponseEntity<Object> newAccount(Authentication authentication){
            if (clientService.findByEmail(authentication.getName()).getAccounts().size() <= 2){
                String accountNumber = randomNumber();
                Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0.0);
                clientService.findByEmail(authentication.getName()).addAccount(newAccount);
                accountService.saveAccount(newAccount);
    }else{
                return  new ResponseEntity<>("You reached the limit of accounts", HttpStatus.FORBIDDEN);
            }
            return  new ResponseEntity<>(HttpStatus.CREATED);

    }
    }

