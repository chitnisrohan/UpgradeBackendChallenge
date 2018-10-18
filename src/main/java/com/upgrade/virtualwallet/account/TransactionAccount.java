package com.upgrade.virtualwallet.account;

import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionAccount implements Transaction {

    private Logger logger = LoggerFactory.getLogger(TransactionAccount.class);
    private Lock balanceChangeLock;

    private Account account;

    public TransactionAccount() {
        balanceChangeLock = new ReentrantLock();
    }

    @Override
    public boolean withdraw(double amount) {
        balanceChangeLock.lock();
        try {
            logger.info("Withdrawing " + amount + " from account : " + account.getAcctNo());
            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
        } finally {
            balanceChangeLock.unlock();
        }
        return false;
    }

    @Override
    public boolean deposit(double amount) {
        return false;
    }

    @Override
    public boolean reverse(double amount) {
        return false;
    }
}
