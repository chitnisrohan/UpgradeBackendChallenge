package com.upgrade.virtualwallet.controller;

import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.service.TransactionService;
import com.upgrade.virtualwallet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/wallet")
public class TransactionController {

    private Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    //Todo figure out how to send json
    @GetMapping(value = "/getBalance/{accountNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public double getBalance(@PathVariable("accountNo") Long accountNo) {
        try {
            return transactionService.findBalance(accountNo);
        } catch (Exception exc) {
            logger.error(String.format("Could not get balance for account number : %d", accountNo));
        }
        return 0.0;
    }

    @PostMapping(value = "/createWallet", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createWallet(@RequestBody User user) {
        try {
            userService.createUser(user);
        } catch (Exception exc) {
            logger.error(String.format("Could not create wallet for user : %s %s",
                    user.getFirstName(), user.getLastName()));
        }
    }
}
