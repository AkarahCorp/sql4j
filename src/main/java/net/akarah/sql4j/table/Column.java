package net.akarah.sql4j.table;

import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.Expression;
import net.akarah.sql4j.value.IntoExpression;
import net.akarah.sql4j.value.Type;

public class Column<T> implements SqlConvertible, IntoExpression<Column<T>> {
    Table table;
    String name;
    Type<T> type;

    private Column() {
    }

    public String name() {
        return this.name;
    }

    public String tabledName() {
        return this.table.name() + "." + this.name();
    }

    public String toSql(Position position) {
        return name
                + " " + type.toSql()
                + (type.isNullable() ? "" : " NOT NULL")
                + ",";
    }

    public static <T> Column<T> of(String name, Type<T> type) {
        var c = new Column<T>();
        c.name = name;
        c.type = type;
        return c;
    }

    public Table table() {
        return this.table;
    }

    @Override
    public Expression<Column<T>> intoExpression() {
        return Expression.of(this);
    }
}
