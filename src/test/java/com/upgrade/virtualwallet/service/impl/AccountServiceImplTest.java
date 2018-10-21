package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.exception.NoDataAvailableException;
import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.models.AccountDTO;
import com.upgrade.virtualwallet.models.AccountType;
import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.repository.AccountRepository;
import com.upgrade.virtualwallet.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findBalance() {
        List<Account> accounts = new LinkedList<>();
        User user = new User("John", "Doe", "121 Main St.", "8889991111");
        accounts.add(new Account("Boston", 7654.90, AccountType.TRANSACTION,
                user, new HashSet<>()));
        when(accountRepository.findByAcctNo(10)).thenReturn(accounts);
        assertEquals(7654.90, accountService.findBalance(10), 0);
    }

    @Test
    public void createAccount() throws Exception {
        User user = new User("John", "Doe", "121 Main St.", "8889991111");
        Account account = new Account("Boston", 7654.90, AccountType.TRANSACTION,
                user, new HashSet<>());
        user.setAccount(null);
        AccountDTO accountDTO = new AccountDTO("Boston", 7654.90, AccountType.TRANSACTION);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(userService.findUser(1l)).thenReturn(user);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        assertEquals("0", accountService.createAccount(accountDTO, 1));

        user.setAccount(account);
        assertEquals("User already has a wallet with account number - 0",
                accountService.createAccount(accountDTO, 1));
    }

    @Test(expected = NoDataAvailableException.class)
    public void createAccountException() throws Exception {
        AccountDTO accountDTO = new AccountDTO("Boston", 7654.90, AccountType.TRANSACTION);
        when(userService.findUser(1l)).thenReturn(null);
        accountService.createAccount(accountDTO, 1l);
    }
}