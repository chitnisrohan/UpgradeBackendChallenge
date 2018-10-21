package com.upgrade.virtualwallet.controller;

import com.upgrade.virtualwallet.models.AccountDTO;
import com.upgrade.virtualwallet.models.AccountType;
import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.service.AccountService;
import com.upgrade.virtualwallet.service.TransactionService;
import com.upgrade.virtualwallet.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class TransactionControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionController transactionController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getBalance() {
        when(accountService.findBalance(1l)).thenReturn(100d);
        assertEquals(HttpStatus.OK, transactionController.getBalance(1l).getStatusCode());
        assertEquals("{\"balance\" : 100.0}", transactionController.getBalance(1l).getBody());
        when(accountService.findBalance(1l)).thenThrow(new RuntimeException());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactionController.getBalance(1l).getStatusCode());
    }

    @Test
    public void createUser() {
        User user = new User("John", "Doe", "121 Main St.", "8889991111");
        assertEquals(HttpStatus.OK, transactionController.createUser(user).getStatusCode());
        doNothing().when(userService).createUser(user);
        doThrow(new RuntimeException()).when(userService).createUser(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactionController.createUser(user).getStatusCode());
    }

    @Test
    public void createWallet() throws Exception {
        AccountDTO accountDTO = new AccountDTO("Boston", 7654.90, AccountType.TRANSACTION);
        when(accountService.createAccount(accountDTO, 1l)).thenReturn("User already has a wallet with account number");
        assertEquals(HttpStatus.BAD_REQUEST, transactionController.createWallet(accountDTO, 1l).getStatusCode());
        when(accountService.createAccount(accountDTO, 1l)).thenReturn("13131");
        assertEquals(HttpStatus.OK, transactionController.createWallet(accountDTO, 1l).getStatusCode());
        when(accountService.createAccount(accountDTO, 1l)).thenThrow(new RuntimeException());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactionController.createWallet(accountDTO, 1l).getStatusCode());
    }

    @Test
    public void withdraw() {
        when(transactionService.withdraw(1l, 100d)).thenReturn(199d);
        assertEquals(HttpStatus.OK, transactionController.withdraw(1l, 100d).getStatusCode());
        assertEquals("{\"message\" : \"New balance after withdrawal is - 199.0\"}", transactionController.withdraw(1l, 100d).getBody());
        when(transactionService.withdraw(1l, 100d)).thenThrow(new RuntimeException());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactionController.withdraw(1l, 100d).getStatusCode());
    }

    @Test
    public void deposit() {
        when(transactionService.deposit(1l, 10d)).thenReturn(99d);
        assertEquals(HttpStatus.OK, transactionController.deposit(1l, 10d).getStatusCode());
        assertEquals("{\"message\" : \"New balance after deposit is - 99.0\"}", transactionController.deposit(1l, 10d).getBody());
        when(transactionService.deposit(1l, 10d)).thenThrow(new RuntimeException());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactionController.deposit(1l, 10d).getStatusCode());
    }

    @Test
    public void reverse() {
        when(transactionService.reverse("123")).thenReturn(true);
        assertEquals(HttpStatus.OK, transactionController.reverse("123").getStatusCode());
        when(transactionService.reverse("123")).thenReturn(false);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactionController.reverse("123").getStatusCode());
        when(transactionService.reverse("123")).thenThrow(new RuntimeException());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactionController.reverse("123").getStatusCode());
    }

    @Test
    public void getNTransactions() {
        when(transactionService.getTransactions(1l, 10)).thenReturn(new LinkedList<>());
        assertEquals(HttpStatus.OK, transactionController.getNTransactions(1l, 10).getStatusCode());
        when(transactionService.getTransactions(1l, 10)).thenThrow(new RuntimeException());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactionController.getNTransactions(1l, 10).getStatusCode());
    }
}