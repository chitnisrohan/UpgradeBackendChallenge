package com.upgrade.virtualwallet.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class TransactionRecord {

    @Id
    private String id;
    private TransactionType transactionType;
    private double amount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    protected TransactionRecord() {
        id = UUID.randomUUID().toString();
    }

    public TransactionRecord(TransactionType transactionType, double amount, Date timestamp) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
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
}
