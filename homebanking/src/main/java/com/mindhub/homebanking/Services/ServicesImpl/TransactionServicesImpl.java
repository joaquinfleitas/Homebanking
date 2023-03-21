package com.mindhub.homebanking.Services.ServicesImpl;

import com.mindhub.homebanking.Services.ServicesTransactions;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServicesImpl implements ServicesTransactions {
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
