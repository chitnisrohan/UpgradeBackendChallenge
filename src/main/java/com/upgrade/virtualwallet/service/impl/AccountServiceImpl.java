package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.exception.NoDataAvailableException;
import com.upgrade.virtualwallet.models.Account;
import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.repository.AccountRepository;
import com.upgrade.virtualwallet.service.AccountService;
import com.upgrade.virtualwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public double findBalance(long acctNo) {
        return accountRepository.findByAcctNo(acctNo).get(0).getBalance();
    }

    @Override
    public Account createAccount(Account account, long userId) throws NoDataAvailableException {
        User user = userService.findUser(userId);
        if (user == null) {
            throw new NoDataAvailableException("User with userId - "+ userId +" is not present");
        }
        account.setUser(user);
        return accountRepository.save(account);
    }
}
