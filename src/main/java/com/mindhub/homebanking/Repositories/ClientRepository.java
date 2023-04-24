package com.mindhub.homebanking.Repositories;

import com.mindhub.homebanking.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
    public interface ClientRepository extends JpaRepository<Client, Long> {
        Client findByEmail(String email);
}


