package com.upgrade.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * Represents a Wallet(account) for the user
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long acctNo;
    private String branch;
    private double balance;
    private AccountType accountType;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    @JsonIgnore
    private Set<TransactionRecord> transactionRecords = new HashSet<>();

    protected Account() {}

    public Account(String branch, double balance, AccountType accountType, User user, Set<TransactionRecord> transactionRecords) {
        this.branch = branch;
        this.balance = balance;
        this.accountType = accountType;
        this.user = user;
        this.transactionRecords = transactionRecords;
    }

    public long getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(long acctNo) {
        this.acctNo = acctNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<TransactionRecord> getTransactionRecords() {
        return transactionRecords;
    }

    public void setTransactionRecords(Set<TransactionRecord> transactionRecords) {
        this.transactionRecords = transactionRecords;
    }
}


