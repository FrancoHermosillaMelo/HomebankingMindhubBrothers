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

import static java.util.stream.Collectors.toList;

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
    @GetMapping("/api/loans")
    public List<LoanDTO> getLoanDTO(){
        return loanService.getLoanDTO();
    }

    @Transactional
    @PostMapping("/api/loans")
        public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApllicationDTO loanApllicationDTO){

        Client selectClient = clientService.findByEmail(authentication.getName());
        Account selectAccount = accountService.findByNumber(loanApllicationDTO.getAccountDestiny().toUpperCase());
        Optional<Loan> selectLoan = loanService.findById(loanApllicationDTO.getLoanID());
//                loanRepository.findById(loanApllicationDTO.getLoanID()).orElse(null);

        Set<ClientLoan> clientLoans;

        if (selectLoan.isEmpty()){
            return new ResponseEntity<>("The selected loan is not available", HttpStatus.FORBIDDEN);
        }else{
            clientLoans =  selectClient.getClientloans().stream().filter(clientLoan -> clientLoan.getLoan().getName().equalsIgnoreCase(selectLoan.get().getName()) && clientLoan.getTotalAmount() > 0).collect(Collectors.toSet());
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
        }else{
            clientLoans = selectClient.getClientloans();
        }
        if (clientLoans.size() == 4){
            return new ResponseEntity<>("You cannot have more than 4 active loans", HttpStatus.FORBIDDEN);
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

    @PostMapping("/api/loans/create")
    public ResponseEntity<Object> newLoan(Authentication authentication,@RequestBody Loan loan){

        Client client = clientService.findByEmail(authentication.getName());

        if(loan.getName().isEmpty()){
            return new ResponseEntity<>("The name of the loan is missing", HttpStatus.FORBIDDEN);
        }
        if(loan.getPayments().isEmpty()){
            return new ResponseEntity<>("Add payments ", HttpStatus.FORBIDDEN);
        }
        if (loan.getMaxAmount() < 1000){
            return new ResponseEntity<>("Loans for less than 1000 cannot be generated", HttpStatus.FORBIDDEN);
        }
        if (loan.getMaxAmount() > 2000000){
            return new ResponseEntity<>("The maximum for loans is 2 million", HttpStatus.FORBIDDEN);
        }
        if (loanService.getLoanDTO().stream().anyMatch(loanDTO -> loanDTO.getName().equalsIgnoreCase(loan.getName()))){
            return new ResponseEntity<>("Loan already exists", HttpStatus.FORBIDDEN);
        }
        if (loan.getInterests() > 2){
            return new ResponseEntity<>("The interest rate cannot exceed 200%", HttpStatus.FORBIDDEN);
        }

        Loan loanAdd = new Loan(loan.getName().toUpperCase(), loan.getMaxAmount(), loan.getPayments(), loan.getInterests());
        loanService.saveLoan(loanAdd);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/api/loans/pay")
    public ResponseEntity<Object> payLoan(Authentication authentication, @RequestParam Long id,@RequestParam String account, @RequestParam Double amount){

        Client client = clientService.findByEmail(authentication.getName());
        ClientLoan clientLoan = clientLoanService.getClientLoan(id);
        Account authenticatedAccount = accountService.findByNumber(account);
        String description = "Pay " + clientLoan.getLoan().getName() + " Loan";

        if(clientLoan == null){
            return new ResponseEntity<>("This loan does not exist", HttpStatus.FORBIDDEN);
        }else if(client == null){
            return new ResponseEntity<>("This client does not exist", HttpStatus.FORBIDDEN);
        }
        if ( account.isBlank() ){
            return new ResponseEntity<>("PLease enter an account", HttpStatus.FORBIDDEN);
        } else if ( client.getAccounts().stream().filter(accounts -> accounts.getNumber().equalsIgnoreCase(account)).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("This account is not yours.", HttpStatus.FORBIDDEN);}
//      amount parameter
        if (clientLoan.getTotalAmount() <= 0) {
            return new ResponseEntity<>("This loan has already been fully paid", HttpStatus.FORBIDDEN);
        }
        if ( amount < 1 ){
            return new ResponseEntity<>("PLease enter an amount bigger than 0", HttpStatus.FORBIDDEN);
        }  else if ( authenticatedAccount.getBalance() < amount ){
            return new ResponseEntity<>("Insufficient balance in your account " + authenticatedAccount.getNumber(), HttpStatus.FORBIDDEN);
        }

        authenticatedAccount.setBalance(authenticatedAccount.getBalance() - amount);

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description , LocalDateTime.now(),true, authenticatedAccount.getBalance());
        authenticatedAccount.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);

        clientLoan.setPayments(clientLoan.getPayments()-1);

        if (clientLoan.getPayments() == 0) {
            clientLoan.setTotalAmount(0.0);
        } else {
            clientLoan.setTotalAmount(clientLoan.getTotalAmount() - amount);
        }


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
