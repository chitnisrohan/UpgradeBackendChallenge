package com.upgrade.virtualwallet.transaction;

public interface Transaction {

    boolean withdraw(double amount);

    boolean deposit(double amount);

    boolean reverse(double amount);
}
