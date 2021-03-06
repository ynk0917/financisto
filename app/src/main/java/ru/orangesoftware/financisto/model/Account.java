/*******************************************************************************
 * Copyright (c) 2010 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     Denis Solonenko - initial API and implementation
 *     Abdsandryk - parameters for bill filtering
 ******************************************************************************/
package ru.orangesoftware.financisto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
public class Account extends MyEntity {

    @Column(name = "card_issuer")
    public String cardIssuer;

    @Column(name = "closing_day")
    public int closingDay;

    @Column(name = "creation_date")
    public long creationDate = System.currentTimeMillis();

    @JoinColumn(name = "currency_id")
    public Currency currency;

    @Column(name = "is_active")
    public boolean isActive = true;

    @Column(name = "is_include_into_totals")
    public boolean isIncludeIntoTotals = true;

    @Column(name = "issuer")
    public String issuer;

    @Column(name = "last_account_id")
    public long lastAccountId;

    @Column(name = "last_category_id")
    public long lastCategoryId;

    @Column(name = "last_transaction_date")
    public long lastTransactionDate = System.currentTimeMillis();

    @Column(name = "total_limit")
    public long limitAmount;

    @Column(name = "note")
    public String note;

    @Column(name = "number")
    public String number;

    @Column(name = "payment_day")
    public int paymentDay;

    @Column(name = "sort_order")
    public int sortOrder;

    @Column(name = "total_amount")
    public long totalAmount;

    @Column(name = "type")
    public String type = AccountType.CASH.name();

    public boolean shouldIncludeIntoTotals() {
        return isActive && isIncludeIntoTotals;
    }
}
