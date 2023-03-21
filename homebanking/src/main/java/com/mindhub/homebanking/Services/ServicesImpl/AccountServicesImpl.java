package com.mindhub.homebanking.Services.ServicesImpl;

import com.mindhub.homebanking.Services.ServicesAccount;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServicesImpl implements ServicesAccount {
    @Autowired
    private AccountRepositories accountRepositories;
    @Override
    public Boolean existsByNumber(String number) {
        return accountRepositories.existsByNumber(number);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepositories.findByNumber(number);
    }

    @Override
    public void save(Account account) {
        accountRepositories.save(account);
    }

    @Override
    public List<Account> findAll() {
        return accountRepositories.findAll();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepositories.findById(id);
    }
}
