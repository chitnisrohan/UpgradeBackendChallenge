package com.upgrade.virtualwallet.models;

/**
 * Used as an incoming DTO to createAccount API
 */
public class AccountDTO {

    private String branch;
    private double balance;
    private AccountType accountType;

    protected AccountDTO() {}

    public AccountDTO(String branch, double balance, AccountType accountType) {
        this.branch = branch;
        this.balance = balance;
        this.accountType = accountType;
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
}
