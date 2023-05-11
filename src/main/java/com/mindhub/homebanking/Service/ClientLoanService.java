package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.ClientLoan;

public interface ClientLoanService {

    ClientLoan getClientLoan(Long id);
    void saveClientLoan(ClientLoan clientLoan);
}
