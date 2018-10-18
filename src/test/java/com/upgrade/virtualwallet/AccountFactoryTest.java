package com.upgrade.virtualwallet;

import com.upgrade.virtualwallet.account.AccountFactory;
import com.upgrade.virtualwallet.account.TransactionAccount;
import com.upgrade.virtualwallet.models.AccountType;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountFactoryTest {

    @Test
    public void getInstance() {
        assertTrue(new AccountFactory().getInstance(AccountType.TRANSACTION) instanceof TransactionAccount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInstanceException() {
        assertTrue(new AccountFactory().getInstance(AccountType.SAVINGS) instanceof TransactionAccount);
    }
}