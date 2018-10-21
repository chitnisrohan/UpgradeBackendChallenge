package com.upgrade.virtualwallet.controller;

import com.upgrade.virtualwallet.models.AccountDTO;
import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.service.AccountService;
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
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/getBalance/{accountNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBalance(@PathVariable("accountNo") Long accountNo) {
        try {
            return new ResponseEntity<>("{\"balance\" : " + accountService.findBalance(accountNo) + "}",
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
    public ResponseEntity<String> createWallet(@RequestBody AccountDTO account, @PathVariable("userId") long userId) {
        try {
            String response = accountService.createAccount(account, userId);
            if (response.contains("User already has a wallet with account number")) {
                return new ResponseEntity<>("{\"message\" : \""+response+"\"}", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(String.format("{\"message\" : \"Account is created successfully. " +
                    "The account number is : %s\"}", response), HttpStatus.OK);
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

    @PutMapping(value = "/deposit/{accountNo}/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deposit(@PathVariable("accountNo") long accountNo,
                                           @PathVariable("amount") double amount) {
        try {
            double newBalance = transactionService.deposit(accountNo, amount);
            return new ResponseEntity<>(String.format("{\"message\" : \"New balance after deposit is - %s\"}", newBalance),
                    HttpStatus.OK);
        } catch (Exception exc) {
            logger.error("Could not deposit amount", exc);
            return new ResponseEntity<>("{\"message\" : \"Deposit failed!\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/reverse/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> reverse(@PathVariable("transactionId") String transactionId) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"message\" : \"Transaction reversal failed!\"}",
                HttpStatus.INTERNAL_SERVER_ERROR);
        try {
            if (transactionService.reverse(transactionId)) {
                responseEntity = new ResponseEntity<>("{\"message\" : \"Transaction reversed successfully!\"}",
                        HttpStatus.OK);
            }
        } catch (Exception exc) {
            logger.error("Could not deposit amount", exc);
        }
        return responseEntity;
    }

    @GetMapping(value = "/getNTransactions/{accountNo}/{number}")
    public ResponseEntity<Object> getNTransactions(@PathVariable("accountNo") long accountNo,
                                                   @PathVariable("number") int number) {
        try {
            return new ResponseEntity<>(transactionService.getTransactions(accountNo, number), HttpStatus.OK);
        } catch(Exception exc) {
            logger.error("Could not retrieve transactions", exc);
            return new ResponseEntity<>("{\"message\" : \"Could not retrieve transactions!\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
