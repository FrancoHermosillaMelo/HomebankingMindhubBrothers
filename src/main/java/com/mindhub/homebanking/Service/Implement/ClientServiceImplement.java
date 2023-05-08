package com.mindhub.homebanking.Service.Implement;

import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;


    @Override
    public List<ClientDTO> getClient() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientAuthentication(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
