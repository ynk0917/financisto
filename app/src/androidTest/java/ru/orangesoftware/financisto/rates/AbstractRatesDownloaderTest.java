/*
 * Copyright (c) 2013 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package ru.orangesoftware.financisto.rates;

import android.test.InstrumentationTestCase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.conn.ConnectTimeoutException;
import ru.orangesoftware.financisto.http.FakeHttpClientWrapper;
import ru.orangesoftware.financisto.model.Currency;

/**
 * Created with IntelliJ IDEA.
 * User: dsolonenko
 * Date: 2/18/13
 * Time: 10:33 PM
 */
public abstract class AbstractRatesDownloaderTest extends InstrumentationTestCase {

    FakeHttpClientWrapper client = new FakeHttpClientWrapper();

    private final AtomicLong counter = new AtomicLong(1);

    private final Map<String, Currency> nameToCurrency = new HashMap<String, Currency>();

    void assertRate(ExchangeRate exchangeRate, String fromCurrency, String toCurrency) {
        assertEquals("Expected " + fromCurrency, currency(fromCurrency).id, exchangeRate.fromCurrencyId);
        assertEquals("Expected " + toCurrency, currency(toCurrency).id, exchangeRate.toCurrencyId);
    }

    void assertRate(ExchangeRate exchangeRate, String fromCurrency, String toCurrency, double rate, long date) {
        assertRate(exchangeRate, fromCurrency, toCurrency);
        assertEquals(rate, exchangeRate.rate, 0.000001);
        assertEquals(date, exchangeRate.date);
    }

    List<Currency> currencies(String... currencies) {
        List<Currency> list = new ArrayList<Currency>();
        for (String name : currencies) {
            list.add(currency(name));
        }
        return list;
    }

    Currency currency(String name) {
        Currency c = nameToCurrency.get(name);
        if (c == null) {
            c = new Currency();
            c.id = counter.getAndIncrement();
            c.name = name;
            nameToCurrency.put(name, c);
        }
        return c;
    }

    ExchangeRate downloadRate(String from, String to) {
        return service().getRate(currency(from), currency(to));
    }

    void givenExceptionWhileRequestingWebService() {
        client.error = new ConnectTimeoutException("Timeout");
    }

    void givenResponseFromWebService(String url, String response) {
        client.givenResponse(url, response);
    }

    abstract ExchangeRateProvider service();

    static String anyUrl() {
        return "*";
    }

}
