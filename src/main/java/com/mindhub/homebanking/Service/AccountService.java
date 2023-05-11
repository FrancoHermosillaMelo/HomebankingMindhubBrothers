package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.DTOS.AccountDTO;
import com.mindhub.homebanking.Models.Account;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();
    List<AccountDTO> getAccountAuthentication(Authentication authentication);
    AccountDTO getAccountID(Long id);
    Account findByNumber(String number);
    Account findById(long id);
    String randomNumberAccount();
    void saveAccount(Account account);

    }

