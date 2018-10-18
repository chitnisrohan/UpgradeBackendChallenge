package com.upgrade.virtualwallet.repository;

import com.upgrade.virtualwallet.models.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Account, Long> {
    List<Account> findByAcctNo(long acctNo);
}
