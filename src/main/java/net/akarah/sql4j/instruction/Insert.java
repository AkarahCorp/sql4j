package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.ExceptionUtils;
import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.Conversions;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.QueryResult;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class Insert<O> implements Instruction<O> {
    Table table;
    List<Column<?>> columns = new ArrayList<>();
    List<Value<?>> values = new ArrayList<>();
    Column<O> returning;

    public static Insert<Void> into(Table.Impl table) {
        var insert = new Insert();
        insert.table = table;
        return insert;
    }

    public <T> Insert<O> withValue(Column<T> column, Value<T> value) {
        this.columns.add(column);
        this.values.add(value);
        return this;
    }

    public <U> Insert<U> returning(Column<U> column) {
        var newInsert = new Insert<U>();
        newInsert.table = this.table;
        newInsert.columns = this.columns;
        newInsert.values = this.values;
        newInsert.returning = column;
        return newInsert;
    }

    @Override
    @SuppressWarnings("unchecked")
    public QueryResult<O> evaluate(Database database) {
        if(this.returning == null) {
            database.execute(this);
            return QueryResult.empty();
        } else {
            var rs = database.evaluate(this);
            var qr = QueryResult.of(rs, 1);
            qr.withFunction(resultSet2 -> {
                var objectList = List.of(
                        ExceptionUtils.sneakyThrows(() -> resultSet2.getObject(this.returning.name()))
                );
                return Conversions.getValueFromList(objectList);
            });
            return (QueryResult<O>) qr;
        }
    }

    @Override
    public String toSql() {
        return "INSERT INTO " +
                this.table.valueSql() +
                StringUtils.parenthesizedValues(this.columns, Column::name) +
                " VALUES " +
                StringUtils.parenthesizedValues(this.values, Value::valueSql) +
                (this.returning == null ? "" : " RETURNING " + this.returning.name());
    }
}
