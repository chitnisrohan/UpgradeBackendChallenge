package com.upgrade.virtualwallet.service;

import com.upgrade.virtualwallet.exception.NoDataAvailableException;
import com.upgrade.virtualwallet.models.Account;

public interface TransactionService {

    double findBalance(long acctNo);

    //todo remove these methods from here and create accountservice
    Account createAccount(Account account, long userId) throws NoDataAvailableException;

    double withdraw(long accountNo, double amount);
}
