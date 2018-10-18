package com.upgrade.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    @JsonIgnore
    private Set<TransactionRecord> transactionRecords = new TreeSet<>(ReverseComparator);

    protected Account() {}

    public Account(String branch, double balance, AccountType accountType, User user, TreeSet<TransactionRecord> transactionRecords) {
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

    private static final Comparator<TransactionRecord> ReverseComparator = new Comparator<TransactionRecord>() {
        @Override
        public int compare(TransactionRecord o1, TransactionRecord o2) {
            if (o1 instanceof TransactionRecord && o2 instanceof TransactionRecord) {
                TransactionRecord transactionRecord1 = o1;
                TransactionRecord transactionRecord2 = o2;
                transactionRecord2.getTimestamp().compareTo(transactionRecord1.getTimestamp());
            }
            return -1;
        }
    };
}


