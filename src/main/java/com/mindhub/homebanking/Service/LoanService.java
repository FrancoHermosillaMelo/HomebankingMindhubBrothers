package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.DTOS.LoanDTO;
import com.mindhub.homebanking.Models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<LoanDTO> getLoanDTO();
    Optional<Loan> findById(long id);
}
