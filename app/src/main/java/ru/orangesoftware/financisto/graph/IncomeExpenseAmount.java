package ru.orangesoftware.financisto.graph;

import java.math.BigDecimal;
import ru.orangesoftware.financisto.report.IncomeExpense;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 7/7/11 12:56 AM
 */
public class IncomeExpenseAmount {

    public BigDecimal expense = BigDecimal.ZERO;

    public BigDecimal income = BigDecimal.ZERO;

    public void add(BigDecimal amount, boolean forceIncome) {
        if (forceIncome || amount.longValue() > 0) {
            income = income.add(amount);
        } else {
            expense = expense.add(amount);
        }
    }

    public long balance() {
        return income.longValue() + expense.longValue();
    }

    public void filter(IncomeExpense incomeExpense) {
        switch (incomeExpense) {
            case INCOME:
                expense = BigDecimal.ZERO;
                break;
            case EXPENSE:
                income = BigDecimal.ZERO;
                break;
            case SUMMARY:
                income = income.add(expense);
                expense = BigDecimal.ZERO;
                break;
        }
    }

    public long max() {
        return Math.max(Math.abs(income.longValue()), Math.abs(expense.longValue()));
    }
}
