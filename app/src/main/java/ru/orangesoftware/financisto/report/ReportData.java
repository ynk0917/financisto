package ru.orangesoftware.financisto.report;

import java.util.List;
import ru.orangesoftware.financisto.graph.GraphUnit;
import ru.orangesoftware.financisto.model.Total;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 2/28/11 9:16 PM
 */
public class ReportData {

    public final Total total;

    public final List<GraphUnit> units;

    public ReportData(List<GraphUnit> units, Total total) {
        this.units = units;
        this.total = total;
    }

}
