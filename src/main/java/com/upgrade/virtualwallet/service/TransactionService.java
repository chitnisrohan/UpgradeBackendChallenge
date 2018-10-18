package com.upgrade.virtualwallet.service;

import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.models.User;

public interface TransactionService {

    Account findByAccountNumber(long acctNo);

    double findBalance(long acctNo);


}
