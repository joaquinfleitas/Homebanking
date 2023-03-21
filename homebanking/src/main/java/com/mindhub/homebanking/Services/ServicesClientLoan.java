package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;

public interface ServicesClientLoan {
    List<ClientLoan> findAll();
    void save(ClientLoan clientLoan);
}
