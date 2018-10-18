package com.upgrade.virtualwallet.models;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

@Entity
public class TransactionRecord implements Comparator {

    @Id
    private String id;
    private TransactionType transactionType;
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    protected TransactionRecord() {
        this.id = UUID.randomUUID().toString();
    }

    public TransactionRecord(TransactionType transactionType, double amount, Account account) {
        this.id = UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = new Date();
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof TransactionRecord && o2 instanceof TransactionRecord) {
            TransactionRecord transactionRecord1 = (TransactionRecord)o1;
            TransactionRecord transactionRecord2 = (TransactionRecord)o2;
            transactionRecord2.getTimestamp().compareTo(transactionRecord1.getTimestamp());
        }
        return -1;
    }
}
