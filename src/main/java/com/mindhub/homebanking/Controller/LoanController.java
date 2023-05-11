package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.LoanApllicationDTO;
import com.mindhub.homebanking.DTOS.LoanDTO;
import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LoanController {
//    @Autowired
//    private ClientRepository clientRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private LoanRepository loanRepository;
//    @Autowired
//    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private TransactionService transactionService;
    @RequestMapping("/api/loans")
    public List<LoanDTO> getLoanDTO(){
        return loanService.getLoanDTO();
    }

    @Transactional
    @RequestMapping(path = "/api/loans", method = RequestMethod.POST)
        public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApllicationDTO loanApllicationDTO){

        Client selectClient = clientService.findByEmail(authentication.getName());
        Account selectAccount = accountService.findByNumber(loanApllicationDTO.getAccountDestiny().toUpperCase());
        Optional<Loan> selectLoan = loanService.findById(loanApllicationDTO.getLoanID());
//                loanRepository.findById(loanApllicationDTO.getLoanID()).orElse(null);

        Set<ClientLoan> clientLoans;

        if (selectLoan.isEmpty()){
            return new ResponseEntity<>("The selected loan is not available", HttpStatus.FORBIDDEN);
        }else{
            clientLoans =  selectClient.getClientloans().stream().filter(clientLoan -> clientLoan.getLoan().getName().equalsIgnoreCase(selectLoan.get().getName())).collect(Collectors.toSet());
        }
        if (!selectLoan.get().getPayments().contains(loanApllicationDTO.getPayments())){
            return new ResponseEntity<>("Installments not available for this loan", HttpStatus.FORBIDDEN);
        }
        if (loanApllicationDTO.getAmount().isNaN()){
            return new ResponseEntity<>("The amount entered is not valid", HttpStatus.FORBIDDEN);
        }
        if (loanApllicationDTO.getAmount() < 1){
            return new ResponseEntity<>("Cannot enter negative amounts", HttpStatus.FORBIDDEN);
        }
        if (selectLoan.get().getMaxAmount() < loanApllicationDTO.getAmount()){
            return new ResponseEntity<>("The amount requested exceeds the maximum amount available", HttpStatus.FORBIDDEN);
        }
        if (selectAccount == null){
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!selectClient.getAccounts().contains(selectAccount)){
            return new ResponseEntity<>("This account does not belong to an authenticated client", HttpStatus.FORBIDDEN);
        }
        if (clientLoans.size() > 0){
            return new ResponseEntity<>("Do you already have a loan of this type", HttpStatus.FORBIDDEN);
        }

        ClientLoan requestedLoan = new ClientLoan(loanApllicationDTO.getAmount(), loanApllicationDTO.getPayments(), loanApllicationDTO.getAmount() * selectLoan.get().getInterests());
        clientLoanService.saveClientLoan(requestedLoan);

        selectAccount.setBalance(selectAccount.getBalance() + loanApllicationDTO.getAmount());

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApllicationDTO.getAmount(), selectLoan.get().getName() + " loan approved", LocalDateTime.now(),true, selectAccount.getBalance());
        transactionService.saveTransaction(transaction);
        selectAccount.addTransaction(transaction);

        selectLoan.get().addClientLoan(requestedLoan);
        selectClient.addClientLoan(requestedLoan);
        clientService.saveClient(selectClient);

        return new ResponseEntity<>("Loan approved ", HttpStatus.CREATED);


    }

}
