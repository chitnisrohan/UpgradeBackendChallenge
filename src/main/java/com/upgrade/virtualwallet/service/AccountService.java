package com.upgrade.virtualwallet.service;

import com.upgrade.virtualwallet.exception.NoDataAvailableException;
import com.upgrade.virtualwallet.models.Account;

public interface AccountService {

    double findBalance(long acctNo);

    Account createAccount(Account account, long userId) throws NoDataAvailableException;
}
