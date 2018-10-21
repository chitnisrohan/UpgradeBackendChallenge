package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.models.*;
import com.upgrade.virtualwallet.repository.AccountRepository;
import com.upgrade.virtualwallet.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.Assert.*;

public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User user;
    private Account account;
    private List<Account> accountList = new LinkedList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User("John", "Doe", "121 Main St.", "8889991111");
        account = new Account("Boston", 7654.90, AccountType.TRANSACTION,
                user, new HashSet<>());
        accountList.add(account);
    }

    @Test
    public void withdraw() {
        when(accountRepository.findByAcctNo(0l)).thenReturn(accountList);
        assertEquals(7000, transactionService.withdraw(0, 654.90), 0);
        assertEquals(6901, transactionService.withdraw(0, 99), 0);
    }

    @Test(expected = IllegalStateException.class)
    public void withdrawException() {
        when(accountRepository.findByAcctNo(0l)).thenReturn(accountList);
        assertEquals(7000, transactionService.withdraw(0, 654.90), 0);
        assertEquals(6901, transactionService.withdraw(0, 9898), 0);
    }

    @Test
    public void deposit() {
        when(accountRepository.findByAcctNo(0l)).thenReturn(accountList);
        assertEquals(7655.90, transactionService.deposit(0, 1), 0);
        assertEquals(7656, transactionService.deposit(0, 0.1), 0);
        assertEquals(8656, transactionService.deposit(0, 1000), 0);
    }

    @Test
    public void reverse() {
        when(accountRepository.findByAcctNo(0l)).thenReturn(accountList);
        TransactionRecord transactionRecord = new TransactionRecord(TransactionType.WITHDRAW, 100d, account);
        transactionRecord.setReversed(false);
        when(transactionRepository.findById("0")).thenReturn(Optional.of(transactionRecord));
        assertTrue(transactionService.reverse("0"));
        assertFalse(transactionService.reverse("0"));

        transactionRecord = new TransactionRecord(TransactionType.DEPOSIT, 100d, account);
        transactionRecord.setReversed(false);
        when(transactionRepository.findById("0")).thenReturn(Optional.of(transactionRecord));
        assertTrue(transactionService.reverse("0"));
        assertFalse(transactionService.reverse("0"));
    }

    @Test
    public void getTransactions() throws Exception {
        Set<TransactionRecord> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            TransactionRecord transactionRecord = new TransactionRecord(TransactionType.DEPOSIT, 100d, account);
            Thread.sleep(1);
            set.add(transactionRecord);
        }
        account.setTransactionRecords(set);
        when(accountRepository.findByAcctNo(0l)).thenReturn(accountList);
        assertEquals(10, transactionService.getTransactions(0l, 10).size());
        account.setAcctNo(1);
        set = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            TransactionRecord transactionRecord = new TransactionRecord(TransactionType.WITHDRAW, 100d, account);
            Thread.sleep(1);
            set.add(transactionRecord);
        }
        when(accountRepository.findByAcctNo(1l)).thenReturn(accountList);
        assertEquals(10, transactionService.getTransactions(1l, 10).size());

    }

}