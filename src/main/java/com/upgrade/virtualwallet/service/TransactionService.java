package com.upgrade.virtualwallet.service;

import com.upgrade.virtualwallet.models.TransactionRecord;

import java.util.List;

public interface TransactionService {

    /**
     * Withdraws given amount from given account number
     * @param accountNo account number to withdraw from
     * @param amount amount to withdraw
     * @return remaining balance after withdrawal
     */
    double withdraw(long accountNo, double amount);

    /**
     * Deposits given amount into given account number
     * @param accountNo account number to deposit into
     * @param amount amount to deposit
     * @return new balance after deposit
     */
    double deposit(long accountNo, double amount);

    /**
     * Reverses the transaction so that account balance is reverted back as if the transaction
     * did not happen.
     * The transaction can be reversed only once.
     * @param id Transaction id
     * @return true if reversal completes successfully, false otherwise.
     */
    boolean reverse(String id);

    /**
     * Retrieves N transactions from given account
     * @param accountNo
     * @param number number of transactions to retrieve
     * @return N transactions for given account number
     */
    List<TransactionRecord> getTransactions(long accountNo, int number);
}
