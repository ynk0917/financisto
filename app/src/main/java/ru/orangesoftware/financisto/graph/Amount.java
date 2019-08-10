/*******************************************************************************
 * Copyright (c) 2010 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     Denis Solonenko - initial API and implementation
 ******************************************************************************/
package ru.orangesoftware.financisto.graph;

import ru.orangesoftware.financisto.model.Currency;
import ru.orangesoftware.financisto.utils.Utils;


public class Amount implements Comparable<Amount> {

    public final long amount;

    public int amountTextHeight;

    public int amountTextWidth;

    public final Currency currency;

    public Amount(Currency currency, long amount) {
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public int compareTo(Amount that) {
        long thisAmount = Math.abs(this.amount);
        long thatAmount = Math.abs(that.amount);
        return thisAmount == thatAmount ? 0 : (thisAmount > thatAmount ? -1 : 1);
    }

    public String getAmountText() {
        return Utils.amountToString(currency, amount, true);
    }

}
