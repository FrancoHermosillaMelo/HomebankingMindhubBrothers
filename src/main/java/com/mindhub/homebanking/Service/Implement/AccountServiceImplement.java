package com.mindhub.homebanking.Service.Implement;

import com.mindhub.homebanking.DTOS.AccountDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(accounts -> new AccountDTO(accounts)).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> getAccountAuthentication(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getAccount().stream().collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountID(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
