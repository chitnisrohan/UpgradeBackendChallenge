package com.upgrade.virtualwallet.repository;

import com.upgrade.virtualwallet.models.TransactionRecord;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionRecord, Long> {

}
