package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.models.TransactionRecord;
import com.upgrade.virtualwallet.models.TransactionType;
import com.upgrade.virtualwallet.repository.AccountRepository;
import com.upgrade.virtualwallet.repository.TransactionRepository;
import com.upgrade.virtualwallet.service.TransactionService;
import com.upgrade.virtualwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Lock balanceChangeLock;

    protected TransactionServiceImpl() {
        balanceChangeLock = new ReentrantLock();
    }

    @Transactional
    @Override
    public double withdraw(long accountNo, double amount) {
        Account account = accountRepository.findByAcctNo(accountNo).get(0);
        double balance = account.getBalance();
        if (balance < amount) {
            throw new IllegalStateException("Cannot withdraw amount greater than available balance.");
        }
        balanceChangeLock.lock();
        try {
            account.setBalance(balance - amount);
            TransactionRecord transactionRecord = new TransactionRecord(TransactionType.WITHDRAW,
                    amount, account);
            account.getTransactionRecords().add(transactionRecord);
            accountRepository.save(account);
            transactionRepository.save(transactionRecord);
        } finally {
            balanceChangeLock.unlock();
        }
        return balance - amount;
    }
}
