package ru.orangesoftware.financisto.db;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import ru.orangesoftware.financisto.model.Account;
import ru.orangesoftware.financisto.model.Category;
import ru.orangesoftware.financisto.model.Transaction;
import ru.orangesoftware.financisto.test.DateTime;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 2/7/11 7:22 PM
 */
public abstract class AbstractDbTest extends AndroidTestCase {

    protected Context context;

    protected DatabaseAdapter db;

    private DatabaseHelper dbHelper;

    public static <T> Set<T> asSet(T... values) {
        return new HashSet<T>(Arrays.asList(values));
    }

    public void assertAccountBalanceForTransaction(Transaction t, Account a, long expectedBalance) {
        long balance = db.getAccountBalanceForTransaction(a, t);
        assertEquals(expectedBalance, balance);
    }

    public void assertAccountTotal(Account account, long total) {
        Account a = db.getAccount(account.id);
        assertEquals("Account " + account.id + " total", total, a.totalAmount);
    }

    public void assertCategory(String name, boolean isIncome, Category c) {
        assertEquals(name, c.title);
        assertEquals(isIncome, c.isIncome());
    }

    public void assertFinalBalanceForAccount(Account account, long expectedBalance) {
        long balance = db.getLastRunningBalanceForAccount(account);
        assertEquals("Account " + account.id + " final balance", expectedBalance, balance);
    }

    public void assertLastTransactionDate(Account account, DateTime dateTime) {
        Account a = db.getAccount(account.id);
        assertEquals("Account " + account.id + " last transaction date", dateTime.asLong(), a.lastTransactionDate);
    }

    public void assertTransactionsCount(Account account, long expectedCount) {
        long count = DatabaseUtils.rawFetchLongValue(db,
                "select count(*) from transactions where from_account_id=?",
                new String[]{String.valueOf(account.id)});
        assertEquals("Transaction for account " + account.id, expectedCount, count);
    }

    @Override
    public void setUp() throws Exception {
        context = new RenamingDelegatingContext(getContext(), "test-");
        dbHelper = new DatabaseHelper(context);
        db = new TestDatabaseAdapter(context, dbHelper);
        db.open();
    }

    @Override
    public void tearDown() throws Exception {
        dbHelper.close();
    }

}
