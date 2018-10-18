package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.repository.TransactionRepository;
import com.upgrade.virtualwallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Account findByAccountNumber(long acctNo) {
        return transactionRepository.findById(acctNo).get();
    }

    @Override
    public double findBalance(long acctNo) {
        return transactionRepository.findByAcctNo(acctNo).get(0).getBalance();
    }
}
