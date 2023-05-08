package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.Models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClient();
    ClientDTO getClientAuthentication(Authentication authentication);
    Client findByEmail(String email);
    void saveClient(Client client);
}
