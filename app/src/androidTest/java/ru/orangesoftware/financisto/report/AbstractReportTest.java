package ru.orangesoftware.financisto.report;

import java.util.List;
import java.util.Map;
import ru.orangesoftware.financisto.db.AbstractDbTest;
import ru.orangesoftware.financisto.filter.WhereFilter;
import ru.orangesoftware.financisto.graph.GraphUnit;
import ru.orangesoftware.financisto.model.Account;
import ru.orangesoftware.financisto.model.Category;
import ru.orangesoftware.financisto.model.Currency;
import ru.orangesoftware.financisto.test.AccountBuilder;
import ru.orangesoftware.financisto.test.CategoryBuilder;
import ru.orangesoftware.financisto.test.CurrencyBuilder;
import ru.orangesoftware.financisto.utils.CurrencyCache;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 7/8/11 1:26 AM
 */
public abstract class AbstractReportTest extends AbstractDbTest {

    Account a1;

    Account a2;

    Account a3;

    Currency c1;

    Currency c2;

    Map<String, Category> categories;

    WhereFilter filter = WhereFilter.empty();

    Report report;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        c1 = CurrencyBuilder.withDb(db).name("USD").title("Dollar").symbol("$").makeDefault().create();
        c2 = CurrencyBuilder.withDb(db).name("SGD").title("Singapore Dollar").symbol("S$").create();
        a1 = AccountBuilder.createDefault(db, c1);
        a2 = AccountBuilder.createDefault(db, c1);
        a3 = AccountBuilder.createDefault(db, c2);
        categories = CategoryBuilder.createDefaultHierarchy(db);
        report = createReport();
        CurrencyCache.initialize(db);
    }

    protected abstract Report createReport();

    void assertExpense(GraphUnit u, long amount) {
        assertEquals(amount, u.getIncomeExpense().expense.longValue());
    }

    void assertIncome(GraphUnit u, long amount) {
        assertEquals(amount, u.getIncomeExpense().income.longValue());
    }

    void assertName(GraphUnit unit, String name) {
        assertEquals(name, unit.name);
    }

    List<GraphUnit> assertReportReturnsData(IncomeExpense incomeExpense) {
        report.setIncomeExpense(incomeExpense);
        ReportData data = report.getReport(db, filter);
        assertNotNull(data);
        List<GraphUnit> units = data.units;
        assertNotNull(units);
        return units;
    }

    List<GraphUnit> assertReportReturnsData() {
        return assertReportReturnsData(IncomeExpense.BOTH);
    }

}
