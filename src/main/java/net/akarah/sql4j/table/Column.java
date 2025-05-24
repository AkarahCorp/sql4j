package net.akarah.sql4j.table;

import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.expr.Values;
import net.akarah.sql4j.value.Type;

public class Column<T> implements SqlConvertible, Value<T> {
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

    public String toDefinition() {
        return name
                + " " + type.toSql()
                + (type.isNullable() ? "" : " NOT NULL");
    }

    public String toSql() {
        return this.tabledName();
    }

    @Override
    public String column() {
        return this.name();
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
}
