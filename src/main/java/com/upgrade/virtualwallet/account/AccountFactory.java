package com.upgrade.virtualwallet.account;

import com.upgrade.virtualwallet.models.AccountType;
import com.upgrade.virtualwallet.transaction.Transaction;

public class AccountFactory {

    Transaction transaction;

    public Transaction getInstance(AccountType accountType) {
        transaction = new TransactionAccount();
        switch (accountType) {
            case TRANSACTION:
                return transaction;
            case SAVINGS:
            default:
                throw new IllegalArgumentException("Given account is not supported for virtual wallet");
        }
    }
}
