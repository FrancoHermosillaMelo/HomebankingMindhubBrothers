package com.mindhub.homebanking.Service.Implement;

import com.mindhub.homebanking.Models.ClientLoan;
import com.mindhub.homebanking.Repositories.ClientLoanRepository;
import com.mindhub.homebanking.Service.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceimplement implements ClientLoanService {
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public ClientLoan getClientLoan(Long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
