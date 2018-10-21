package com.upgrade.virtualwallet.service;

import com.upgrade.virtualwallet.exception.NoDataAvailableException;
import com.upgrade.virtualwallet.models.AccountDTO;

public interface AccountService {

    /**
     * retrieve balance from account number
     * @param acctNo
     * @return account balance
     */
    double findBalance(long acctNo);

    /**
     * Creates new account in database (Wallet). There can be only 1 account per user.
     * @param account account info
     * @param userId account holder id
     * @return new Account number as string if successful OR error message if failed
     * @throws NoDataAvailableException throws exception when user does not exists
     */
    String createAccount(AccountDTO account, long userId) throws NoDataAvailableException;
}
