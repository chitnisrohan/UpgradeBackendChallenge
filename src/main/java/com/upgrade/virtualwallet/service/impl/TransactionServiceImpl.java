package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.models.TransactionRecord;
import com.upgrade.virtualwallet.models.TransactionType;
import com.upgrade.virtualwallet.repository.AccountRepository;
import com.upgrade.virtualwallet.repository.TransactionRepository;
import com.upgrade.virtualwallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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

    @Transactional
    @Override
    public double deposit(long accountNo, double amount) {
        Account account = accountRepository.findByAcctNo(accountNo).get(0);
        double balance = account.getBalance();
        balanceChangeLock.lock();
        try {
            account.setBalance(balance + amount);
            TransactionRecord transactionRecord = new TransactionRecord(TransactionType.DEPOSIT,
                    amount, account);
            account.getTransactionRecords().add(transactionRecord);
            accountRepository.save(account);
            transactionRepository.save(transactionRecord);
        } finally {
            balanceChangeLock.unlock();
        }
        return balance + amount;
    }

    @Transactional
    @Override
    public boolean reverse(String id) {
        boolean result = false;
        if (transactionRepository.findById(id).isPresent()) {
            TransactionRecord transactionRecord = transactionRepository.findById(id).get();
            // transaction can be reversed only once
            if (transactionRecord.isReversed()) {
                result = false;
            } else if (transactionRecord.getTransactionType() == TransactionType.DEPOSIT) {
                transactionRecord.setReversed(true);
                transactionRepository.save(transactionRecord);
                withdraw(transactionRecord.getAccount().getAcctNo(), transactionRecord.getAmount());
                result = true;
            } else if (transactionRecord.getTransactionType() == TransactionType.WITHDRAW) {
                transactionRecord.setReversed(true);
                transactionRepository.save(transactionRecord);
                deposit(transactionRecord.getAccount().getAcctNo(), transactionRecord.getAmount());
                result = true;
            }
        }
        return result;
    }

    @Override
    public List<TransactionRecord> getTransactions(long accountNo, int number) {
        Account account = accountRepository.findByAcctNo(accountNo).get(0);
        Set<TransactionRecord> transactionRecords = account.getTransactionRecords();
        Set<TransactionRecord> sortedSet = new TreeSet<>((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
        sortedSet.addAll(transactionRecords);
        return sortedSet.stream().limit(number).collect(Collectors.toList());
    }
}
