package com.upgrade.virtualwallet.models;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long acctNo;
    private String branch;
    private double balance;
    private AccountType accountType;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    protected Account() {}

    public Account(String branch, double balance, AccountType accountType) {
        this.branch = branch;
        this.balance = balance;
        this.accountType = accountType;
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
}
