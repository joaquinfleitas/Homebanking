package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepositories extends JpaRepository<Account, Long> {
    Boolean existsByNumber(String number);
    Account findByNumber(String number);
}


//REPASAR