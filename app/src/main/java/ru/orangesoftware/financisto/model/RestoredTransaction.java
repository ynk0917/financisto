package ru.orangesoftware.financisto.model;

import java.util.Date;

public class RestoredTransaction {

    public final Date dateTime;

    public final long transactionId;

    public RestoredTransaction(long transactionId, Date dateTime) {
        this.transactionId = transactionId;
        this.dateTime = dateTime;
    }

}
