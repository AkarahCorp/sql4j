package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.QueryResult;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class Insert implements Instruction<Void> {
    Table table;
    List<Column<?>> columns = new ArrayList<>();
    List<Value<?>> values = new ArrayList<>();

    public static Insert into(Table table) {
        var insert = new Insert();
        insert.table = table;
        return insert;
    }

    public <T> Insert withValue(Column<T> column, Value<T> value) {
        this.columns.add(column);
        this.values.add(value);
        return this;
    }

    @Override
    public QueryResult<Void> evaluate(Database database) {
        database.execute(this);
        return QueryResult.empty();
    }

    @Override
    public String toSql() {
        return "INSERT INTO " +
                this.table.name() +
                StringUtils.parenthesizedValues(this.columns, Column::name) +
                " VALUES " +
                StringUtils.parenthesizedValues(this.values, SqlConvertible::toSql) +
                ";";
    }
}
