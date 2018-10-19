package com.upgrade.virtualwallet.service;

import com.upgrade.virtualwallet.exception.NoDataAvailableException;
import com.upgrade.virtualwallet.models.Account;

public interface AccountService {

    /**
     * retrieve balance from account number
     * @param acctNo
     * @return account balance
     */
    double findBalance(long acctNo);

    /**
     * Creates new account in database (Wallet)
     * @param account account info
     * @param userId account holder id
     * @return new account created
     * @throws NoDataAvailableException throws exception when user does not exists
     */
    Account createAccount(Account account, long userId) throws NoDataAvailableException;
}
