package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.AccountType;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Service.AccountService;
import com.mindhub.homebanking.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClient();
    }
    @GetMapping("/api/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return clientService.getClientAuthentication(authentication);
    }

    @PostMapping("/api/clients")

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank()) {
            return new ResponseEntity<>("Your name is missing.", HttpStatus.FORBIDDEN);
        } else if (!firstName.matches("^[a-z A-Z]*$")) {
            return new ResponseEntity<>("Please enter a valid FirstName. Only letters are allowed.", HttpStatus.FORBIDDEN);
        }

        if (lastName.isBlank()) {
            return new ResponseEntity<>("Your last name is missing.", HttpStatus.FORBIDDEN);
        }
        else if (!lastName.matches("^[a-z A-Z]*$")) {
            return new ResponseEntity<>("Please enter a valid LastName. Only letters are allowed.", HttpStatus.FORBIDDEN);
        }

        if (email.isBlank()) {
            return new ResponseEntity<>("Your email is missing.", HttpStatus.FORBIDDEN);
        }
        else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return new ResponseEntity<>("Please enter a valid Email address.", HttpStatus.FORBIDDEN);
        }

        if (password.isBlank()) {
            return new ResponseEntity<> ("Your password is missing", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }


        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);
        String accountNumber = accountService.randomNumberAccount();
        Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0.0,true, AccountType.CURRENT);
        newClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}