package com.upgrade.virtualwallet.models;

public enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    REVERSAL;

    public TransactionType getReverse(TransactionType type) {
        if (type == TransactionType.DEPOSIT) {
            return WITHDRAW;
        } else if (type == TransactionType.WITHDRAW) {
            return DEPOSIT;
        }
        throw new IllegalArgumentException("Reversal not possible");
    }
}
