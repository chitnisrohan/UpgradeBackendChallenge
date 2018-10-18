package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.exception.NoDataAvailableException;
import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.repository.TransactionRepository;
import com.upgrade.virtualwallet.service.TransactionService;
import com.upgrade.virtualwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    private Lock balanceChangeLock;

    protected TransactionServiceImpl() {
        balanceChangeLock = new ReentrantLock();
    }

    @Override
    public double findBalance(long acctNo) {
        return transactionRepository.findByAcctNo(acctNo).get(0).getBalance();
    }

    @Override
    public Account createAccount(Account account, long userId) throws NoDataAvailableException {
        User user = userService.findUser(userId);
        if (user == null) {
            throw new NoDataAvailableException("User with userId - "+ userId +" is not present");
        }
        account.setUser(user);
        return transactionRepository.save(account);
    }

    @Override
    public double withdraw(long accountNo, double amount) {
        Account account = transactionRepository.findByAcctNo(accountNo).get(0);
        double balance = account.getBalance();
        if (balance < amount) {
            throw new IllegalStateException("Cannot withdraw amount greater than available balance.");
        }
        balanceChangeLock.lock();
        try {
            account.setBalance(balance - amount);
            transactionRepository.save(account);
        } finally {
            balanceChangeLock.unlock();
        }
        return balance - amount;
    }
}
