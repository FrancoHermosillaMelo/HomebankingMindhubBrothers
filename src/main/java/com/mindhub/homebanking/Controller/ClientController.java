package com.mindhub.homebanking.Controller;

import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ClientController {
    @Autowired
    private ClientRepository repository;

    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients(){
        return repository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return repository.findById(id).map(c ->  new ClientDTO(c)).orElse(null);
    };


}
