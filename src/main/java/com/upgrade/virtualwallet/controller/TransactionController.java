package com.upgrade.virtualwallet.controller;

import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.service.TransactionService;
import com.upgrade.virtualwallet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/wallet")
public class TransactionController {

    private Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/getBalance/{accountNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBalance(@PathVariable("accountNo") Long accountNo) {
        try {
            return new ResponseEntity<>("{\"balance\" : " + transactionService.findBalance(accountNo) + "}",
                    HttpStatus.OK);
        } catch (Exception exc) {
            logger.error(String.format("Could not get balance for account number : %d", accountNo), exc);
            return new ResponseEntity<>("{\"message\" : \"Could not retrieve account balance\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return new ResponseEntity<>(String.format("{\"message\" : \"User %s %s created successfully\"}",
                    user.getFirstName(), user.getLastName()), HttpStatus.OK);
        } catch (Exception exc) {
            logger.error(String.format("Could not create wallet for user : %s %s",
                    user.getFirstName(), user.getLastName()), exc);
            return new ResponseEntity<>("{\"message\" : \"User creation failed. Please contact administrator.\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/createWallet/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createWallet(@RequestBody Account account, @PathVariable("userId") long userId) {
        try {
            Account newAccount = transactionService.createAccount(account, userId);
            return new ResponseEntity<>(String.format("{\"message\" : \"Account is created successfully. " +
                    "The account number is : %d\"}", newAccount.getAcctNo()), HttpStatus.OK);
        } catch (Exception exc) {
            logger.error("Account creation failed.", exc);
            return new ResponseEntity<>("{\"message\" : \"Account creation failed. Please contact administrator.\"}"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/withdraw/{accountNo}/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> withdraw(@PathVariable("accountNo") long accountNo,
                           @PathVariable("amount") double amount) {
        try {
            double newBalance = transactionService.withdraw(accountNo, amount);
            return new ResponseEntity<>(String.format("{\"message\" : \"New balance after withdrawal is - %s\"}", newBalance),
                    HttpStatus.OK);
        } catch (Exception exc) {
            logger.error("Could not withdraw amount", exc);
            return new ResponseEntity<>("{\"message\" : \"Withdrawal failed!\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
