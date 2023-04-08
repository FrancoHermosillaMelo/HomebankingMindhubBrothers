package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.AccountDTO;
import com.mindhub.homebanking.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountController {
        @Autowired
        private AccountRepository repository;

        @RequestMapping("/api/accounts")
        public List<AccountDTO> getAccounts(){
            return repository.findAll().stream().map(accounts -> new AccountDTO(accounts)).collect(Collectors.toList());
        }

        @RequestMapping("/api/accounts/{id}")
            public AccountDTO getAccount(@PathVariable Long id) {
                return repository.findById(id).map(a ->  new AccountDTO(a)).orElse(null);
        }
    }

